package com.n1.moguchi.ui.fragment.parent.goal_creation

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
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
import java.util.ArrayList
import javax.inject.Inject

class GoalCreationFragment : Fragment() {

    private var _binding: FragmentGoalCreationBinding? = null
    private val binding get() = _binding!!

    private lateinit var childrenAdapter: ChildrenRecyclerAdapter

    private var childSelectionEnable: Boolean = false
    private var addChildButtonEnable: Boolean = false
    private var isFromOnBoarding: Boolean = false
    private var isInBottomSheetShouldOpen: Boolean = false

    private var isNextButtonPressed: Boolean? = null

    private var childId: String? = null
    private var selectedChildIndex = 0
    private var childrenSize = 0
    private var goalHeight: Int = 0

    private val goalsForParse: MutableList<Goal> = mutableListOf()

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

        parseArgs()

        if (parentId != null) {
            if (isFromOnBoarding) {
                // get parcelable bundle from previous fragment in AfterOnBoardingFragment (ChildCreationFragment)
                parentFragment?.requireArguments()?.getParcelableArrayList<Child>("children")?.let {
                    if (it.isNotEmpty()) {
                        viewModel.setChildrenFromBundle(it.toList())
                    }
                }
            } else {
                viewModel.getChildren(parentId)
            }

            viewModel.children.observe(viewLifecycleOwner) { children ->
                childrenSize = children.size

                // TODO - Not correct implementation of rv after deleting all related parent data and then register again
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
                childId?.let {
                    viewModel.createGoal(
                        binding.goalTitle.text.toString(),
                        goalHeight,
                        it
                    )
                }

                if (!isInBottomSheetShouldOpen) {
                    onBoardingSection(bundle)
                }
            }
        }

        viewModel.goal.observe(viewLifecycleOwner) {
            if (it != null) {
                goalsForParse.add(it)
                parentFragment?.arguments?.putParcelableArrayList(
                    "goals",
                    goalsForParse as ArrayList<out Parcelable>
                )
                val newBundle = Bundle().apply {
                    this.putString(CHILD_ID_KEY, childId)
                    this.putString(GOAL_ID_KEY, it.goalId)
                    this.putParcelable(
                        it.goalId,
                        it
                    )
                }
                parentFragmentManager.setFragmentResult("goalCreationRequestKey", newBundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onBoardingSection(bundle: Bundle) {
        if (selectedChildIndex < childrenSize) {
            selectedChildIndex++
            if (selectedChildIndex == childrenSize) {
                selectedChildIndex = 0
            }
        }
        this.arguments = bundle
    }

    private fun parseArgs() {
        addChildButtonEnable = requireArguments().getBoolean(ADD_CHILD_BUTTON_ENABLE_KEY)
        childSelectionEnable = requireArguments().getBoolean(CHILD_SELECTION_ENABLE_KEY)
        isInBottomSheetShouldOpen = requireArguments().getBoolean(INSIDE_BOTTOM_SHEET_OPEN_KEY)
        isFromOnBoarding = requireArguments().getBoolean(IS_FROM_ON_BOARDING_KEY)
    }

    private fun setupRecyclerView(
        children: List<Child>,
        selectedChildIndex: Int,
        childSelectionEnable: Boolean
    ) {
        val recyclerView: RecyclerView = binding.rvChildrenList
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        childrenAdapter = ChildrenRecyclerAdapter(
            children.toMutableList(),
            selectedChildIndex,
            addChildButtonEnable,
            childSelectionEnable
        )
        recyclerView.adapter = childrenAdapter
    }

    companion object {
        const val GOAL_ID_KEY = "goalIdKey"
        const val CHILD_ID_KEY = "childIdKey"

        private const val ADD_CHILD_BUTTON_ENABLE_KEY = "addChildButtonEnable"
        private const val CHILD_SELECTION_ENABLE_KEY = "childSelectionEnable"
        private const val INSIDE_BOTTOM_SHEET_OPEN_KEY = "insideBottomSheetShouldOpen"
        private const val IS_FROM_ON_BOARDING_KEY = "isFromOnBoarding"

        fun newInstance(
            addChildButtonEnable: Boolean,
            childSelectionEnable: Boolean,
            insideBottomSheetShouldOpen: Boolean,
            isFromOnBoarding: Boolean
        ): GoalCreationFragment {
            return GoalCreationFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(
                        CHILD_SELECTION_ENABLE_KEY,
                        childSelectionEnable
                    )
                    putBoolean(
                        INSIDE_BOTTOM_SHEET_OPEN_KEY,
                        insideBottomSheetShouldOpen
                    )
                    putBoolean(
                        ADD_CHILD_BUTTON_ENABLE_KEY,
                        addChildButtonEnable
                    )
                    putBoolean(
                        IS_FROM_ON_BOARDING_KEY,
                        isFromOnBoarding
                    )
                }
            }
        }
    }
}