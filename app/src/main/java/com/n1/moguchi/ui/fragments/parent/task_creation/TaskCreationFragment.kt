package com.n1.moguchi.ui.fragments.parent.task_creation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.databinding.FragmentTaskCreationBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.adapters.TaskCreationRecyclerAdapter
import com.n1.moguchi.ui.fragments.parent.SecondaryBottomSheetFragment
import com.n1.moguchi.ui.fragments.parent.goal_creation.GoalCreationFragment
import javax.inject.Inject

class TaskCreationFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentTaskCreationBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskCreationRecyclerAdapter: TaskCreationRecyclerAdapter

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentGoalID = requireParentFragment().arguments?.getString(GoalCreationFragment.GOAL_ID_KEY)

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_card_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        taskCreationRecyclerAdapter = TaskCreationRecyclerAdapter()
        recyclerView.adapter = taskCreationRecyclerAdapter
        recyclerView.recycledViewPool.setMaxRecycledViews(
            TaskCreationRecyclerAdapter.VIEW_TYPE_TASK_CARD,
            TaskCreationRecyclerAdapter.MAX_POOL_SIZE
        )
        if (currentGoalID != null) {
            viewModel.getTasksByGoalId(currentGoalID)
        }
        viewModel.tasks.observe(viewLifecycleOwner) {
            if (it.isEmpty() && (currentGoalID != null)) {
                taskCreationRecyclerAdapter.tasksCardList.add(
                    0,
                    viewModel.createTask(Task(), currentGoalID)
                )
                taskCreationRecyclerAdapter.notifyItemInserted(0)
                taskCreationRecyclerAdapter.notifyItemChanged(taskCreationRecyclerAdapter.itemCount - 1)
            }

            taskCreationRecyclerAdapter.onNewTaskAddClicked = {
                if (currentGoalID != null) {
                    taskCreationRecyclerAdapter.tasksCardList.add(viewModel.createTask(Task(), currentGoalID))
                    taskCreationRecyclerAdapter.notifyItemInserted(taskCreationRecyclerAdapter.itemCount - 1)
                }
            }

            taskCreationRecyclerAdapter.onTaskSettingsClicked = {
                showTaskSettingsBottomSheet()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showTaskSettingsBottomSheet() {
        val fragmentManager = childFragmentManager
        val modalBottomSheet = SecondaryBottomSheetFragment()
        modalBottomSheet.show(fragmentManager, SecondaryBottomSheetFragment.TAG)
    }

    companion object {
        const val TAG = "TaskCreationBottomSheet"
    }
}