package com.n1.moguchi.ui.fragments.parent

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
import com.n1.moguchi.ui.adapters.TaskSetupRecyclerAdapter
import com.n1.moguchi.ui.viewmodels.TaskCreationViewModel
import javax.inject.Inject

class TaskCreationFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentTaskCreationBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskSetupRecyclerAdapter: TaskSetupRecyclerAdapter
    private var tasksCardList: MutableList<Task> = mutableListOf()

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

        val currentGoalID = arguments?.getString(GoalCreationFragment.GOAL_ID_KEY)

        if (currentGoalID != null) {
            viewModel.getTasksByGoalId(currentGoalID)
        }
        viewModel.tasks.observe(viewLifecycleOwner) {
            val recyclerView: RecyclerView = view.findViewById(R.id.rv_card_list)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            taskSetupRecyclerAdapter = TaskSetupRecyclerAdapter()
            recyclerView.adapter = taskSetupRecyclerAdapter

            if (it.isEmpty() && currentGoalID != null) {
                taskSetupRecyclerAdapter.tasksCard.add(
                    0,
                    viewModel.createTask(Task(), currentGoalID)
                )
                tasksCardList.add(viewModel.createTask(Task(), currentGoalID))
            }

            taskSetupRecyclerAdapter.onTaskSettingsClicked = {
                showTaskSettingsBottomSheet()
            }
        }

        binding.addTaskButton.setOnClickListener {
//            tasksCardList.add(taskCard)
            taskSetupRecyclerAdapter.notifyItemInserted(0)
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