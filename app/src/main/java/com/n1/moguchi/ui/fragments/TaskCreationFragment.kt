package com.n1.moguchi.ui.fragments

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
import com.n1.moguchi.databinding.FragmentTaskCreationBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.adapters.TaskListAdapter
import com.n1.moguchi.ui.viewmodels.PrimaryBottomSheetViewModel
import javax.inject.Inject

class TaskCreationFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTaskCreationBinding
    private lateinit var taskListAdapter: TaskListAdapter

    private var tasksCardList: MutableList<View> = mutableListOf()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: PrimaryBottomSheetViewModel by lazy {
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskCreationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_card_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        taskListAdapter = TaskListAdapter(tasksCardList)
        recyclerView.adapter = taskListAdapter

        binding.addTaskButton.setOnClickListener {
            val taskCard =
                layoutInflater.inflate(R.layout.task_creation_card, recyclerView, false)
            tasksCardList.add(taskCard)
            taskListAdapter.notifyItemInserted(0)
        }
    }

    companion object {
        const val TAG = "TaskCreationBottomSheet"
    }
}