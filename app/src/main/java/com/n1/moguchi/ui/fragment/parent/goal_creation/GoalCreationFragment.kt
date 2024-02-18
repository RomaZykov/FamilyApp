package com.n1.moguchi.ui.fragment.parent.goal_creation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.databinding.FragmentGoalCreationBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.adapter.ChildrenRecyclerAdapter
import java.util.UUID
import javax.inject.Inject

class GoalCreationFragment(
    private val addChildButtonEnable: Boolean,
    private val childSelectionEnable: Boolean,
    private val isInBottomSheetShouldOpen: Boolean
) : Fragment() {

    private var _binding: FragmentGoalCreationBinding? = null
    private val binding get() = _binding!!

    private lateinit var childrenAdapter: ChildrenRecyclerAdapter

    private var isNextButtonPressed: Boolean? = null
    private var childId: String? = null
    private var selectedChildIndex = 0
    private var childrenSize = 0
    private var goalHeight: Int = 0

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: GoalCreationViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GoalCreationViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as MoguchiBaseApplication).appComponent
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalCreationBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parentId = Firebase.auth.currentUser?.uid

        if (parentId != null) {
            viewModel.getChildren(parentId)
            viewModel.children.observe(viewLifecycleOwner) { children ->
                childrenSize = children.size
                if (selectedChildIndex < children.size) {
                    setupRecyclerView(children, selectedChildIndex, childSelectionEnable)
                    childId = children[selectedChildIndex].childId
                }

                childrenAdapter.onChildClicked = { childIndex, _ ->
                    selectedChildIndex = childIndex
                    childId = children[selectedChildIndex].childId
                }
            }
        } else {
            throw Exception("User not authorized")
        }

        viewModel.totalGoalPoints.observe(viewLifecycleOwner) {
            goalHeight = it
        }

        binding.goalTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                val regex = "^[a-zA-Zа-яА-Я0-9 ]+$".toRegex()
                if (text.toString().isNotBlank() && text.toString().matches(regex)) {
                    parentFragmentManager.setFragmentResult(
                        "isButtonEnabledRequestKey",
                        bundleOf("buttonIsReadyKey" to true)
                    )
                    viewModel.setGoalTitle(text.toString())
                } else {
                    binding.goalTitle.error = getString(R.string.error_goal_title)
                    parentFragmentManager.setFragmentResult(
                        "isButtonEnabledRequestKey",
                        bundleOf("buttonIsReadyKey" to false)
                    )
                }
            }
        })

        binding.goalIncreaseButton.setOnClickListener {
            viewModel.increaseGoalHeight()
        }

        binding.goalDecreaseButton.setOnClickListener {
            viewModel.decreaseGoalHeight()
        }

        parentFragmentManager.setFragmentResultListener(
            "nextButtonPressedRequestKey",
            viewLifecycleOwner
        ) { _, bundle ->
            isNextButtonPressed = bundle.getBoolean("buttonIsPressedKey")
            if (isNextButtonPressed == true) {
                val goalId: String = UUID.randomUUID().toString()

                val newBundle = Bundle()
                newBundle.putString(CHILD_ID_KEY, childId)
                newBundle.putString(GOAL_ID_KEY, goalId)
                parentFragmentManager.setFragmentResult("goalCreationRequestKey", newBundle)

                if (!isInBottomSheetShouldOpen) {
                    onBoardingSection(bundle)
                }

                childId?.let {
                    viewModel.createGoal(
                        Goal(
                            goalId = goalId,
                            title = binding.goalTitle.text.toString(),
                            totalPoints = goalHeight
                        ),
                        it
                    )
                }
            }
        }
    }

    private fun onBoardingSection(bundle: Bundle) {
        if (selectedChildIndex < childrenSize) {
            selectedChildIndex++
            bundle.putBoolean(GOAL_SETTING_FOR_CHILDREN_COMPLETED_KEY, false)
            if (selectedChildIndex == childrenSize) {
                bundle.putBoolean(GOAL_SETTING_FOR_CHILDREN_COMPLETED_KEY, true)
                selectedChildIndex = 0
            }
        }
        this.arguments = bundle
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView(
        children: List<Child>,
        selectedChildIndex: Int,
        childSelectionEnable: Boolean
    ) {
        val recyclerView: RecyclerView = binding.rvChildrenList
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        childrenAdapter =
            ChildrenRecyclerAdapter(
                children,
                selectedChildIndex,
                addChildButtonEnable,
                childSelectionEnable
            )
        recyclerView.adapter = childrenAdapter
    }

    companion object {
        const val GOAL_ID_KEY = "goalIDKey"
        const val CHILD_ID_KEY = "childIDKey"

        const val GOAL_SETTING_FOR_CHILDREN_COMPLETED_KEY = "goalSettingForChildrenCompletedKey"
    }
}