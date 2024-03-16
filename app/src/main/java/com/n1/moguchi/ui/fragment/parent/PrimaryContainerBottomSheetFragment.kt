package com.n1.moguchi.ui.fragment.parent

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.databinding.FragmentPrimaryBottomSheetBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.fragment.parent.child_creation.ChildCreationFragment
import com.n1.moguchi.ui.fragment.parent.goal_creation.GoalCreationFragment
import com.n1.moguchi.ui.fragment.parent.task_creation.TaskCreationFragment
import javax.inject.Inject

class PrimaryContainerBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentPrimaryBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var childrenToSave = listOf<Child>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PrimaryContainerViewModel::class.java]
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
            this.arguments = bundle
            when (bundle.getString("primaryBundleKey")) {
                "TaskCreationIntent" -> {
                    binding.title.text = getString(R.string.new_tasks)
                    childFragmentManager.setFragmentResultListener(
                        "isButtonEnabledRequestKey",
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
                }

                "ChildCreationIntent" -> {
                    binding.title.text = getString(R.string.add_child_title)
                    binding.nextButton.text = getString(R.string.done)
                    binding.nextButton.setCompoundDrawablesRelative(null, null, null, null)
                    childFragmentManager.setFragmentResultListener(
                        "isButtonEnabledRequestKey",
                        viewLifecycleOwner
                    ) { _, innerBundle ->
                        val isButtonEnabled = innerBundle.getBoolean("buttonIsReadyKey")
                        binding.nextButton.isEnabled = isButtonEnabled
                    }
                    childFragmentManager.commit {
                        replace(
                            R.id.primary_child_fragment_container,
                            ChildCreationFragment.newInstance(
                                isFromOnBoarding = false,
                                isFromParentProfile = false,
                                isFromParentHome = true,
                                deleteChildOptionEnable = true
                            ),
                            TO_CHILD_CREATION_TAG
                        )
                    }
                }

                "GoalCreationIntentMainActivity" -> {
                    binding.title.text = getString(R.string.new_goal)
                    childFragmentManager.setFragmentResultListener(
                        "isButtonEnabledRequestKey",
                        viewLifecycleOwner
                    ) { _, innerBundle ->
                        val isButtonEnabled = innerBundle.getBoolean("buttonIsReadyKey")
                        binding.nextButton.isEnabled = isButtonEnabled
                    }
                    childFragmentManager.commit {
                        replace(
                            R.id.primary_child_fragment_container,
                            GoalCreationFragment.newInstance(
                                addChildButtonEnable = true,
                                childSelectionEnable = true,
                                insideBottomSheetShouldOpen = true,
                                isFromOnBoarding = false
                            ),
                            TO_TASK_CREATION_TAG
                        )
                    }
                }
            }
        }

        binding.nextButton.setOnClickListener {
            val currentFragmentInContainer = childFragmentManager.fragments[0]
            when (currentFragmentInContainer.tag) {
                TO_CHILD_CREATION_TAG -> {
                    childFragmentManager.setFragmentResult(
                        "nextButtonPressedRequestKey",
                        bundleOf("buttonIsPressedKey" to true)
                    )
                    childFragmentManager.setFragmentResultListener(
                        "createBundleRequestKey",
                        viewLifecycleOwner
                    ) { _, innerBundle ->
                        childrenToSave =
                            innerBundle.getParcelableArrayList<Child>("children")?.toList()
                                ?: emptyList()
                    }

                    if (childrenToSave.isNotEmpty()) {
                        viewModel.saveChildrenToDb(childrenToSave)
                        val childrenBundle = Bundle().apply {
                            this.putParcelableArrayList(
                                "children",
                                childrenToSave.toMutableList() as ArrayList<out Parcelable>
                            )
                        }
                        parentFragmentManager.setFragmentResult(
                            "refreshRecyclerViewRequestKey",
                            childrenBundle
                        )
                    }
                    dismiss()
                }

                TO_TASK_CREATION_TAG -> {
                    childFragmentManager.setFragmentResult(
                        "nextButtonPressedRequestKey",
                        bundleOf("buttonIsPressedKey" to true)
                    )
                    childFragmentManager.setFragmentResultListener(
                        "goalCreationRequestKey",
                        viewLifecycleOwner
                    ) { _, innerBundle ->
                        if (arguments != null) {
                            this.arguments?.putAll(innerBundle)
                        } else {
                            this.arguments = innerBundle
                        }
                    }
                    childFragmentManager.commit {
                        remove(currentFragmentInContainer)
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

                    childFragmentManager.setFragmentResult(
                        "nextButtonPressedRequestKey",
                        bundleOf("buttonIsPressedKey" to true)
                    )

                    val args = requireArguments()
                    val goal = args.getParcelableArrayList<Goal>("goals")?.get(0)
                    val tasks = args.getParcelableArrayList<Task>("tasks")
                    viewModel.saveGoalWithTasksToDb(goal!!, tasks?.toList()!!)

                    childFragmentManager.commit {
                        remove(currentFragmentInContainer)
                        replace<SuccessGoalAddedFragment>(R.id.primary_child_fragment_container)
                    }
                }

                TO_TASKS_COMPLETE_TAG -> {
                    binding.title.visibility = View.GONE
                    binding.bottomLinearLayout.visibility = View.GONE
                    binding.primaryChildFragmentContainer.visibility = View.GONE

                    childFragmentManager.setFragmentResult(
                        "nextButtonPressedRequestKey",
                        bundleOf("buttonIsPressedKey" to true)
                    )

                    childFragmentManager.commit {
                        remove(currentFragmentInContainer)
                        replace<SuccessTasksAddedFragment>(R.id.full_fragment_container)
                    }

                    val args = requireArguments()
                    val tasks = args.getParcelableArrayList<Task>("tasks")
                    viewModel.saveTasksToDb(tasks?.toList()!!)
                    val tasksBundle = Bundle().apply {
                        this.putParcelableArrayList(
                            "tasks",
                            tasks as ArrayList<out Parcelable>
                        )
                    }
                    parentFragmentManager.setFragmentResult(
                        "refreshRecyclerViewRequestKey",
                        tasksBundle
                    )

                    childFragmentManager.setFragmentResultListener(
                        "tasksAddedRequestKey",
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
        private const val TO_TASK_CREATION_TAG = "ToTaskCreationFragment"
        private const val TO_CHILD_CREATION_TAG = "ToChildCreationFragment"
        private const val TO_GOAL_COMPLETE_TAG = "ToSuccessGoalFragment"
        private const val TO_TASKS_COMPLETE_TAG = "ToSuccessTaskFragment"
    }
}
