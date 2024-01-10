package com.n1.moguchi.ui.fragment.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.setFragmentResultListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentPrimaryBottomSheetBinding
import com.n1.moguchi.ui.fragment.parent.goal_creation.GoalCreationFragment
import com.n1.moguchi.ui.fragment.parent.task_creation.TaskCreationFragment

class PrimaryBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentPrimaryBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrimaryBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modalBottomSheet = view.findViewById<ConstraintLayout>(R.id.primary_modal_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        setFragmentResultListener("requestKey") { _, bundle ->
            when (bundle.getString("bundleKey")) {
                "TaskCreationIntent" -> {
                    childFragmentManager.commit {
                        replace(
                            R.id.primary_child_fragment_container,
                            TaskCreationFragment(),
                            TO_TASKS_COMPLETE_TAG
                        )
                    }
                    binding.title.text = getString(R.string.new_task)
                    binding.nextButton.text = getString(R.string.ready_button)
                    binding.nextButton.setCompoundDrawablesRelative(null, null, null, null)
                }

                "MainActivity" -> {
                    childFragmentManager.commit {
                        replace(
                            R.id.primary_child_fragment_container,
                            GoalCreationFragment(),
                            TO_TASK_CREATION_TAG
                        )
                    }
                }
            }
        }

        binding.nextButton.setOnClickListener {
            val currentFragmentInContainer = childFragmentManager.fragments[0]
            when (currentFragmentInContainer.tag) {
                TO_TASK_CREATION_TAG -> {
                    childFragmentManager.commit {
                        remove(currentFragmentInContainer)
                        replace(
                            R.id.primary_child_fragment_container,
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
                        replace<SuccessGoalAddedFragment>(R.id.primary_child_fragment_container)
                    }
                }

                TO_TASKS_COMPLETE_TAG -> {
                    binding.title.visibility = View.GONE
                    binding.bottomLinearLayout.visibility = View.GONE
                    binding.primaryChildFragmentContainer.visibility = View.GONE
                    childFragmentManager.commit {
                        remove(currentFragmentInContainer)
                        replace<SuccessTasksAddedFragment>(R.id.full_fragment_container)
                    }
                    childFragmentManager.setFragmentResultListener(
                        "secondaryRequestKey",
                        viewLifecycleOwner
                    ) { _, bundle ->
                        if (bundle.getBoolean("buttonPressedKey")) {
                            dismiss()
                        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TO_TASK_CREATION_TAG = "TaskCreationFragment"
        private const val TO_GOAL_COMPLETE_TAG = "SuccessGoalFragment"
        private const val TO_TASKS_COMPLETE_TAG = "SuccessTaskFragment"
    }
}