package com.n1.moguchi.ui.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentTasksBinding
import com.n1.moguchi.ui.adapters.TasksListRecyclerAdapter

class TasksFragment : Fragment() {

    private lateinit var binding: FragmentTasksBinding
    private lateinit var tasksListRecyclerAdapter: TasksListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.rvTasksList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        tasksListRecyclerAdapter = TasksListRecyclerAdapter()
        recyclerView.adapter = tasksListRecyclerAdapter

        val topAppBar = requireActivity().findViewById<Toolbar>(R.id.top_tasks_app_bar)
        topAppBar.setNavigationOnClickListener {
            parentFragmentManager.commit {
                remove(this@TasksFragment)
            }
        }

        binding.addTaskFab.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTag = TAG
            setFragmentResult("requestKey", bundleOf("bundleKey" to fragmentTag))
            val bottomSheet = PrimaryBottomSheetFragment()
            bottomSheet.show(fragmentManager, TAG)
        }
    }

    companion object {
        private const val TAG = "TasksFragment"
    }
}