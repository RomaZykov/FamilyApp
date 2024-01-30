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
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.databinding.FragmentTaskCreationBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.fragment.parent.TaskSettingsSecondaryBottomSheetFragment
import com.n1.moguchi.ui.fragment.parent.goal_creation.GoalCreationFragment
import javax.inject.Inject

class TaskCreationFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentTaskCreationBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskCreationRecyclerAdapter: TaskCreationRecyclerAdapter

    private var isNextButtonPressed: Boolean? = null

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

        val recyclerView: RecyclerView = binding.rvCardList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        taskCreationRecyclerAdapter = TaskCreationRecyclerAdapter()
        recyclerView.adapter = taskCreationRecyclerAdapter
        recyclerView.recycledViewPool.setMaxRecycledViews(
            TaskCreationRecyclerAdapter.VIEW_TYPE_TASK_CARD,
            TaskCreationRecyclerAdapter.MAX_POOL_SIZE
        )

        val currentGoalID =
            requireParentFragment().arguments?.getString(GoalCreationFragment.GOAL_ID_KEY)
        if (currentGoalID != null) {
            viewModel.setupMaxPointsOfGoal(currentGoalID)
            viewModel.getTasksByGoalId(currentGoalID)
        }
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            if (tasks.isEmpty() && (currentGoalID != null)) {
                taskCreationRecyclerAdapter.tasksCardList.add(
                    0,
                    viewModel.createTask(Task(), currentGoalID)
                )
                taskCreationRecyclerAdapter.notifyItemInserted(0)
                taskCreationRecyclerAdapter.notifyItemChanged(taskCreationRecyclerAdapter.itemCount - 1)
            }

            taskCreationRecyclerAdapter.onNewTaskAddClicked = {
                if (currentGoalID != null) {
                    taskCreationRecyclerAdapter.tasksCardList.add(
                        viewModel.createTask(
                            Task(),
                            currentGoalID
                        )
                    )
                    taskCreationRecyclerAdapter.notifyItemInserted(tasks.size)
                    taskCreationRecyclerAdapter.notifyItemChanged(taskCreationRecyclerAdapter.itemCount - 1)
                    if (taskCreationRecyclerAdapter.tasksCardList.size == 2) {
                        taskCreationRecyclerAdapter.notifyItemChanged(0)
                    }
                }
            }

            taskCreationRecyclerAdapter.onTaskUpdate =
                { updatedTask, taskPointsChanged, taskTitleChanged ->
                    viewModel.updateTask(updatedTask, taskPointsChanged, taskTitleChanged)
                }

            taskCreationRecyclerAdapter.onTaskDeleteClicked = { task ->
                if (currentGoalID != null) {
                    viewModel.deleteTask(currentGoalID, task)
                    if (taskCreationRecyclerAdapter.tasksCardList.size == 2) {
                        taskCreationRecyclerAdapter.notifyItemChanged(0)
                    }
                }
            }

            taskCreationRecyclerAdapter.onCardsStatusUpdate = { isAllTasksCompleted ->
                parentFragmentManager.setFragmentResult(
                    "buttonIsEnabled",
                    bundleOf("buttonIsReadyKey" to isAllTasksCompleted)
                )
            }

            taskCreationRecyclerAdapter.onTaskSettingsClicked = {
                showTaskSettingsBottomSheet()
            }

            parentFragmentManager.setFragmentResultListener(
                "nextButtonPressedRequestKey",
                viewLifecycleOwner
            ) { _, bundle ->
                isNextButtonPressed = bundle.getBoolean("buttonIsPressedKey")
                if (isNextButtonPressed == true) {
                    parentFragment?.arguments?.putParcelableArrayList(
                        "tasks",
                        tasks as ArrayList<out Parcelable>
                    )
                }
            }
        }
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