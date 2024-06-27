package com.n1.moguchi.ui.fragment.parent.task_creation

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.data.remote.model.Goal
import com.n1.moguchi.data.remote.model.Task
import com.n1.moguchi.databinding.FragmentTaskCreationBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.fragment.parent.TaskSettingsSecondaryBottomSheetFragment
import com.n1.moguchi.ui.fragment.parent.goal_creation.GoalCreationFragment.Companion.GOAL_ID_KEY
import javax.inject.Inject

class TaskCreationFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentTaskCreationBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskCreationRecyclerAdapter: TaskCreationRecyclerAdapter

    private var isNextButtonPressed: Boolean? = null
    private var tasksForParse: MutableList<Task> = mutableListOf()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: TaskCreationViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TaskCreationViewModel::class.java]
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
        _binding = FragmentTaskCreationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        val currentGoalId =
            requireParentFragment().requireArguments().getString(GOAL_ID_KEY)
        val relatedGoal =
            requireParentFragment().requireArguments().getParcelable<Goal>(currentGoalId)

        if (relatedGoal != null) {
            viewModel.setupGoal(relatedGoal)
        } else {
            viewModel.setupMaxPointsOfGoal(currentGoalId!!)
        }

        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            if (tasks.isEmpty()) {
                taskCreationRecyclerAdapter.tasksCardList.add(
                    0,
                    viewModel.returnCreatedTask(
                        currentGoalId!!
                    )
                )
                taskCreationRecyclerAdapter.notifyItemInserted(0)
                taskCreationRecyclerAdapter.notifyItemChanged(taskCreationRecyclerAdapter.itemCount - 1)
            }

            taskCreationRecyclerAdapter.onNewTaskAddClicked = {
                taskCreationRecyclerAdapter.tasksCardList.add(
                    viewModel.returnCreatedTask(
                        currentGoalId!!
                    )
                )
                if (taskCreationRecyclerAdapter.tasksCardList.size == 2) {
                    taskCreationRecyclerAdapter.notifyItemChanged(0)
                }
                taskCreationRecyclerAdapter.notifyItemInserted(tasks.size)
                taskCreationRecyclerAdapter.notifyItemChanged(taskCreationRecyclerAdapter.itemCount - 1)
            }

            taskCreationRecyclerAdapter.onTaskDeleteClicked = { task, position ->
                if (position == 0 && taskCreationRecyclerAdapter.tasksCardList.size == 2) {
                    taskCreationRecyclerAdapter.notifyItemChanged(1)
                }
                if (position == 1 && taskCreationRecyclerAdapter.tasksCardList.size == 2) {
                    taskCreationRecyclerAdapter.notifyItemChanged(0)
                }
                viewModel.deleteTask(task)
            }

            taskCreationRecyclerAdapter.onCardsStatusUpdate = { isAllTasksCompleted ->
                parentFragmentManager.setFragmentResult(
                    "isButtonEnabledRequestKey",
                    bundleOf("buttonIsReadyKey" to isAllTasksCompleted)
                )
            }

            taskCreationRecyclerAdapter.onTaskSettingsClicked = {
                showTaskSettingsBottomSheet()
            }
        }

        viewModel.totalGoalPoints.observe(viewLifecycleOwner) {
            binding.goalProgressBar.max = it
        }

        viewModel.currentGoalPoints.observe(viewLifecycleOwner) {
            binding.goalProgressBar.progress = it
            binding.taskHeightTotal.text = it.toString()
            taskCreationRecyclerAdapter.onTaskUpdate = { updatedTask, shouldIncreasePoints ->
                viewModel.onTaskUpdate(updatedTask, shouldIncreasePoints)
            }
        }

        parentFragmentManager.setFragmentResultListener(
            "nextButtonPressedRequestKey",
            viewLifecycleOwner
        ) { _, bundle ->
            tasksForParse.addAll(taskCreationRecyclerAdapter.tasksCardList)
            isNextButtonPressed = bundle.getBoolean("buttonIsPressedKey")
            if (isNextButtonPressed == true) {
                parentFragment?.arguments?.putParcelableArrayList(
                    "tasks",
                    tasksForParse as ArrayList<out Parcelable>
                )
            }
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.rvCardList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        taskCreationRecyclerAdapter = TaskCreationRecyclerAdapter()
        recyclerView.adapter = taskCreationRecyclerAdapter
        recyclerView.recycledViewPool.setMaxRecycledViews(
            TaskCreationRecyclerAdapter.VIEW_TYPE_TASK_CARD,
            TaskCreationRecyclerAdapter.MAX_POOL_SIZE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showTaskSettingsBottomSheet() {
        val fragmentManager = childFragmentManager
        val modalBottomSheet = TaskSettingsSecondaryBottomSheetFragment()
        modalBottomSheet.show(fragmentManager, TaskSettingsSecondaryBottomSheetFragment.TAG)
    }
}