package com.n1.moguchi.ui.fragment.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.databinding.FragmentTasksBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.adapter.TasksRecyclerAdapter
import com.n1.moguchi.ui.fragment.parent.PrimaryBottomSheetFragment
import javax.inject.Inject

class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private lateinit var tasksRecyclerAdapter: TasksRecyclerAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TasksViewModel::class.java]
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
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = requireActivity().findViewById<Toolbar>(R.id.top_tasks_app_bar)
        topAppBar.setNavigationOnClickListener {
            parentFragmentManager.commit {
                remove(this@TasksFragment)
            }
        }

        val relatedGoalId = arguments?.getString("goalId")
        if (relatedGoalId != null) {
            viewModel.setCurrentProgression(relatedGoalId)
            viewModel.fetchActiveTasks(relatedGoalId)
            viewModel.fetchCompletedTasks(relatedGoalId)
        }

        viewModel.currentAndTotalGoalPoints.observe(viewLifecycleOwner) {
            binding.tasksPoints.text = getString(
                R.string.current_total_goal_points,
                it.keys.toString().trim('[', ']').toInt(),
                it.values.toString().trim('[', ']').toInt()
            )
        }

        viewModel.activeTasks.observe(viewLifecycleOwner) {
            binding.activeTasks.text = getString(R.string.active_tasks, it.size)
            setupRecyclerViewByRelatedTasks(relatedGoalId, it, true)
            binding.activeTasks.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    setupRecyclerViewByRelatedTasks(relatedGoalId, it, true)
                }
            }
        }

        viewModel.completedTasks.observe(viewLifecycleOwner) {
            binding.completedTasks.text = getString(R.string.completed_tasks, it?.size ?: 0)
            binding.completedTasks.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    setupRecyclerViewByRelatedTasks(relatedGoalId, it, false)
                }
            }
        }

        binding.addTaskFab.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTag = TASK_CREATION_TAG
            setFragmentResult("requestKey", bundleOf("bundleKey" to fragmentTag))
            val bottomSheet = PrimaryBottomSheetFragment()
            bottomSheet.show(fragmentManager, TASK_CREATION_TAG)
        }
    }

    private fun setupRecyclerViewByRelatedTasks(
        relatedGoalId: String?,
        relatedTasks: List<Task>,
        isActive: Boolean
    ) {
        val recyclerView: RecyclerView = binding.rvTasksList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        tasksRecyclerAdapter = TasksRecyclerAdapter(relatedTasks, isActive)
        recyclerView.adapter = tasksRecyclerAdapter

        tasksRecyclerAdapter.onTaskDeleteClicked = { task, isActiveTask ->
            if (relatedGoalId != null) {
                viewModel.deleteTask(relatedGoalId, task, isActiveTask)
            }
        }

        tasksRecyclerAdapter.onTaskStatusChangedClicked = { task, isActiveTask ->
            viewModel.updateTaskStatus(task, isActiveTask)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TASK_CREATION_TAG = "TaskCreationIntent"
    }
}
