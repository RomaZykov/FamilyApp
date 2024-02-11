package com.n1.moguchi.ui.fragment.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.setFragmentResultListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentPrimaryBottomSheetBinding
import com.n1.moguchi.ui.fragment.parent.children_creation.ChildCreationFragment
import com.n1.moguchi.ui.fragment.parent.goal_creation.GoalCreationFragment
import com.n1.moguchi.ui.fragment.parent.task_creation.TaskCreationFragment

class PrimaryContainerBottomSheetFragment : BottomSheetDialogFragment() {

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

        setFragmentResultListener("primaryBottomSheetRequestKey") { _, bundle ->
            when (bundle.getString("primaryBundleKey")) {
                "TaskCreationIntent" -> {
                    this.arguments = bundle
                    childFragmentManager.setFragmentResultListener(
                        "buttonIsEnabled",
                        viewLifecycleOwner
                    ) { _, innerBundle ->
                        val isButtonEnabled = innerBundle.getBoolean("buttonIsReadyKey")
                        binding.nextButton.isEnabled = isButtonEnabled
                    }
                    childFragmentManager.commit {
                        replace(
                            R.id.primary_child_fragment_container,
                            TaskCreationFragment(),
                            TO_TASKS_COMPLETE_TAG
                        )
                    }
                    binding.title.text = getString(R.string.new_tasks)
                    binding.nextButton.text = getString(R.string.done)
                    binding.nextButton.setCompoundDrawablesRelative(null, null, null, null)
                }

                "ChildCreationIntent" -> {
                    childFragmentManager.commit {
                        replace(
                            R.id.primary_child_fragment_container,
                            ChildCreationFragment(deleteChildOptionEnable = false),
                            TO_CHILD_CREATION_TAG
                        )
                    }
                    binding.title.text = getString(R.string.add_child_title)
                    binding.nextButton.text = getString(R.string.done)
                    binding.nextButton.setCompoundDrawablesRelative(null, null, null, null)
                }

                "GoalCreationIntentMainActivity" -> {
                    binding.title.text = getString(R.string.new_goal)
                    childFragmentManager.setFragmentResultListener(
                        "buttonIsEnabled",
                        viewLifecycleOwner
                    ) { _, innerBundle ->
                        val isButtonEnabled = innerBundle.getBoolean("buttonIsReadyKey")
                        binding.nextButton.isEnabled = isButtonEnabled
                    }
                    childFragmentManager.commit {
                        replace(
                            R.id.primary_child_fragment_container,
                            GoalCreationFragment(
                                addChildButtonEnable = true,
                                childSelectionEnable = true,
                                isInBottomSheetShouldOpen = true
                            ),
                            TO_TASK_CREATION_TAG
                        )
                    }
                }
            }
        }

        binding.nextButton.setOnClickListener {
            childFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
                when (fragment.tag) {
                    TO_TASK_CREATION_TAG -> {
                        fragmentManager.setFragmentResult(
                            "nextButtonPressedRequestKey",
                            bundleOf("buttonIsPressedKey" to true)
                        )
                        fragmentManager.setFragmentResultListener(
                            "goalCreationRequestKey",
                            viewLifecycleOwner
                        ) { _, bundle ->
                            if (arguments != null) {
                                this.arguments?.clear()
                            }
                            this.arguments = bundle
                        }
                        fragmentManager.commit {
                            remove(fragment)
                            replace(
                                R.id.primary_child_fragment_container,
                                TaskCreationFragment(),
                                TO_GOAL_COMPLETE_TAG
                            )
                        }
                        binding.nextButton.text = getString(R.string.done)
                        binding.nextButton.setCompoundDrawablesRelative(null, null, null, null)
                    }

                    TO_GOAL_COMPLETE_TAG -> {
                        binding.nextButton.visibility = View.GONE
                        binding.cancelButton.visibility = View.GONE
                        binding.space.visibility = View.GONE
                        binding.title.visibility = View.GONE
                        binding.bottomLinearLayout.findViewById<Button>(R.id.add_goal_button).visibility =
                            View.VISIBLE
                        fragmentManager.commit {
                            remove(fragment)
                            replace<SuccessGoalAddedFragment>(R.id.primary_child_fragment_container)
                        }
                    }

                    TO_TASKS_COMPLETE_TAG -> {
                        binding.title.visibility = View.GONE
                        binding.bottomLinearLayout.visibility = View.GONE
                        binding.primaryChildFragmentContainer.visibility = View.GONE
                        fragmentManager.commit {
                            remove(fragment)
                            replace<SuccessTasksAddedFragment>(R.id.full_fragment_container)
                        }

                        fragmentManager.setFragmentResultListener(
                            "tasksAddedRequestKey",
                            viewLifecycleOwner
                        ) { _, bundle ->
                            if (bundle.getBoolean("buttonPressedKey")) {
                                dismiss()
                                parentFragmentManager
                            }
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
        private const val TO_CHILD_CREATION_TAG = "ChildCreationFragment"
        private const val TO_GOAL_COMPLETE_TAG = "SuccessGoalFragment"
        private const val TO_TASKS_COMPLETE_TAG = "SuccessTaskFragment"
    }
}
