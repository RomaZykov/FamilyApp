package com.n1.moguchi.ui.fragment.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.models.remote.Task
import com.n1.moguchi.databinding.FragmentTasksBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.adapter.TasksRecyclerAdapter
import com.n1.moguchi.ui.fragment.parent.PrimaryContainerBottomSheetFragment
import com.n1.moguchi.ui.fragment.parent.goal_creation.GoalCreationFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private lateinit var tasksRecyclerAdapter: TasksRecyclerAdapter
    private var currentProfileMode: String? = null

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
            parentFragmentManager.popBackStack()
        }

        val relatedGoalId = arguments?.getString("goalId")

        lifecycleScope.launch {
            viewModel.getUserPrefs().collect {
                currentProfileMode = it.currentProfileMode
            }
        }

        if (relatedGoalId != null) {
            viewModel.setupRelatedGoalDetails(relatedGoalId)
            viewModel.fetchCompletedTasks(relatedGoalId)
            viewModel.fetchActiveTasks(relatedGoalId)
        }

        viewModel.currentAndTotalGoalPoints.observe(viewLifecycleOwner) {
            setProgression(it.keys.toString(), it.values.toString())
        }

        viewModel.activeTasks.observeOnce(viewLifecycleOwner) {
            initRecyclerViewByRelatedTasks(
                it.toMutableList(),
                false,
                currentProfileMode!!
            )
        }
        recyclerViewCallbacks(relatedGoalId)

        viewModel.activeTasks.observe(viewLifecycleOwner) { activeTasks ->
            binding.activeTasks.text = getString(R.string.active_tasks, activeTasks.size)
            binding.activeTasks.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && currentProfileMode != null) {
                    initRecyclerViewByRelatedTasks(
                        activeTasks.toMutableList(),
                        true,
                        currentProfileMode!!
                    )
                }
            }
        }

        viewModel.completedTasks.observe(viewLifecycleOwner) { completedTasks ->
            binding.completedTasks.text =
                getString(R.string.completed_tasks, completedTasks?.size ?: 0)
            binding.completedTasks.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && currentProfileMode != null) {
                    initRecyclerViewByRelatedTasks(
                        completedTasks.toMutableList(),
                        false,
                        currentProfileMode!!
                    )
                }
            }
        }

        binding.addTaskFab.setOnClickListener {
            childFragmentManager.setFragmentResult(
                "primaryBottomSheetRequestKey",
                bundleOf(
                    "primaryBundleKey" to TASK_CREATION_TAG,
                    GoalCreationFragment.GOAL_ID_KEY to relatedGoalId
                )
            )
            childFragmentManager.setFragmentResultListener(
                "refreshRecyclerViewRequestKey",
                viewLifecycleOwner
            ) { _, innerBundle ->
                val addedTasks =
                    innerBundle.getParcelableArrayList<Task>("tasks")
                        ?.toList()
                if (addedTasks != null) {
                    tasksRecyclerAdapter.updateTasksList = addedTasks.toMutableList()
                    tasksRecyclerAdapter.notifyItemRangeInserted(
                        tasksRecyclerAdapter.itemCount - 1,
                        addedTasks.size
                    )
                }
            }
            val bottomSheet = PrimaryContainerBottomSheetFragment()
            bottomSheet.show(childFragmentManager, TASK_CREATION_TAG)
        }
    }

    private fun recyclerViewCallbacks(relatedGoalId: String?) {
        if (this::tasksRecyclerAdapter.isInitialized) {
            tasksRecyclerAdapter.onTaskDeleteClicked = { task, isActiveTask ->
                if (relatedGoalId != null) {
                    viewModel.deleteTask(relatedGoalId, task, isActiveTask)
                    // TODO - Implement progression to complete goal
                    viewModel.updateRelatedGoal(relatedGoalId, -task.height)
                }
            }

            tasksRecyclerAdapter.onTaskStatusChangedClicked = { task, isActiveTask ->
                if (relatedGoalId != null) {
                    viewModel.updateTaskStatus(task, isActiveTask)
                    // TODO - Implement progression to complete goal
                    viewModel.updateRelatedGoal(relatedGoalId, task.height)
                }
            }
        }
    }

    private fun initRecyclerViewByRelatedTasks(
        relatedTasks: MutableList<Task>,
        isActive: Boolean,
        profileMode: String
    ) {
        val recyclerView: RecyclerView = binding.rvTasksList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        tasksRecyclerAdapter =
            TasksRecyclerAdapter(relatedTasks, isActive, profileMode = profileMode)
        recyclerView.adapter = tasksRecyclerAdapter
    }

    private fun setProgression(cPoints: String, tPoints: String) {
        val currentPoints = cPoints.trim('[', ']').toInt()
        val totalPoints = tPoints.trim('[', ']').toInt()
        binding.tasksPoints.text = getString(
            R.string.current_total_goal_points,
            currentPoints,
            totalPoints
        )
        binding.tasksProgressBar.max = totalPoints
        binding.tasksProgressBar.progress = currentPoints
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(
            lifecycleOwner,
            object : Observer<T> {
                override fun onChanged(value: T) {
                    observer.onChanged(value)
                    removeObserver(this)
                }
            }
        )
    }

    companion object {
        private const val TASK_CREATION_TAG = "TaskCreationIntent"
    }
}