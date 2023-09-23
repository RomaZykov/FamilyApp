package com.n1.moguchi.ui.fragments.parent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentPrimaryBottomSheetBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.viewmodels.PrimaryBottomSheetViewModel
import javax.inject.Inject

class PrimaryBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPrimaryBottomSheetBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PrimaryBottomSheetViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as MoguchiBaseApplication).appComponent
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrimaryBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modalBottomSheet = view.findViewById<ConstraintLayout>(R.id.primary_modal_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        setFragmentResultListener("requestKey") { key, bundle ->
            val result = bundle.getString("bundleKey")
            when (result) {
                "TasksFragment" -> {
                    childFragmentManager.commit {
                        replace(
                            R.id.child_fragment_container,
                            TaskCreationFragment(),
                            TO_TASKS_COMPLETE_TAG
                        )
                    }
                    binding.nextButton.text = "Готово"
                    binding.nextButton.setCompoundDrawablesRelative(null, null, null, null)
                }

                "MainActivity" -> {
                    childFragmentManager.commit {
                        replace(
                            R.id.child_fragment_container,
                            GoalCreationFragment(),
                            TO_TASK_CREATION_TAG
                        )
                    }
                }
            }
        }
        binding.nextButton.setOnClickListener {
            val currentGoalHeight = viewModel.goalHeight.value
//            val goal = Goal(
//
//            )
//            viewModel.createGoal(goal = goal)
            val currentFragmentInContainer = childFragmentManager.fragments[0]
            when (currentFragmentInContainer.tag) {
                TO_TASK_CREATION_TAG -> {
                    childFragmentManager.commit {
                        remove(currentFragmentInContainer)
                        replace(
                            R.id.child_fragment_container,
                            TaskCreationFragment(),
                            TO_GOAL_COMPLETE_TAG
                        )
                    }
                    binding.nextButton.text = "Готово"
                    binding.nextButton.setCompoundDrawablesRelative(null, null, null, null)
                }

                TO_GOAL_COMPLETE_TAG -> {
                    binding.nextButton.visibility = View.GONE
                    binding.cancelButton.visibility = View.GONE
                    binding.space.visibility = View.GONE
                    binding.title.visibility = View.GONE
                    binding.bottomLinearLayout.findViewById<Button>(R.id.add_goal_button).visibility =
                        View.VISIBLE
                    childFragmentManager.commit {
                        remove(currentFragmentInContainer)
                        replace<SuccessGoalAddedFragment>(R.id.child_fragment_container)
                    }
                }

                TO_TASKS_COMPLETE_TAG -> {
                    binding.title.visibility = View.GONE
                    binding.bottomLinearLayout.visibility = View.GONE
                    binding.childFragmentContainer.visibility = View.GONE
                    childFragmentManager.commit {
                        remove(currentFragmentInContainer)
                        replace<SuccessTasksAddedFragment>(R.id.full_fragment_container)
                    }
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        binding.addGoalButton.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        private const val TO_TASK_CREATION_TAG = "TaskCreationFragment"
        private const val TO_GOAL_COMPLETE_TAG = "SuccessGoalFragment"
        private const val TO_TASKS_COMPLETE_TAG = "SuccessTaskFragment"
    }
}