package com.n1.moguchi.ui.fragments.parent.goal_creation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.databinding.FragmentGoalCreationBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.adapters.ChildrenRecyclerAdapter
import java.util.UUID
import javax.inject.Inject

class GoalCreationFragment : Fragment() {

    private var _binding: FragmentGoalCreationBinding? = null
    private val binding get() = _binding!!
    private lateinit var childrenAdapter: ChildrenRecyclerAdapter
    private var isNextButtonPressed: Boolean? = null
    private var goalHeight: Int = 0
    private var childId: String? = null

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
                setupRecyclerView(children)
                childId = children[childrenAdapter.selectedItem].childId
            }
        } else {
            throw Exception("User not authorized")
        }

        viewModel.goalHeight.observe(viewLifecycleOwner) {
            goalHeight = it
        }

        binding.goalName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                if (text.toString().isNotEmpty()) {
                    parentFragmentManager.setFragmentResult(
                        "buttonIsEnabled",
                        bundleOf("buttonIsReadyKey" to true)
                    )
                    viewModel.setGoalTitle(text.toString())
                } else {
                    binding.goalName.error = "Добавьте цель"
                    parentFragmentManager.setFragmentResult(
                        "buttonIsEnabled",
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
            "nextButtonPressed",
            viewLifecycleOwner
        ) { _, bundle ->
            isNextButtonPressed = bundle.getBoolean("buttonIsPressedKey")
            if (isNextButtonPressed == true) {
                val goalId: String = UUID.randomUUID().toString()
                parentFragment?.arguments?.putString(GOAL_ID_KEY, goalId)
                arguments?.putString(GOAL_ID_KEY, goalId)
                bundle.putString(GOAL_ID_KEY, goalId)
                Log.d("GoalCreationFragment", "Bundle = ${bundle}")
                viewModel.createGoal(
                    Goal(
                        goalId = goalId,
                        title = binding.goalName.text.toString(),
                        height = goalHeight
                    ),
                    childId!!
                )
            }
        }
    }

    private fun setupRecyclerView(children: List<Child>) {
        val recyclerView: RecyclerView = binding.rvChildrenList
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        childrenAdapter = ChildrenRecyclerAdapter(children)
        recyclerView.adapter = childrenAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val GOAL_ID_KEY = "goalID"
    }
}