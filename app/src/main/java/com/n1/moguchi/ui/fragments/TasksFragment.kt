package com.n1.moguchi.ui.fragments

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
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentTasksBinding
import com.n1.moguchi.ui.adapters.CompletedTasksRecyclerAdapter
import com.n1.moguchi.ui.adapters.TasksRecyclerAdapter
import com.n1.moguchi.ui.fragments.parent.PrimaryBottomSheetFragment

class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViewByTasks(TasksRecyclerAdapter())

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

        binding.activeTasks.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) {
                button.isChecked = true
                button.isEnabled = false
                binding.completedTasks.isChecked = false
                binding.completedTasks.isEnabled = true
                setupRecyclerViewByTasks(TasksRecyclerAdapter())
            }
        }

        binding.completedTasks.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) {
                button.isChecked = true
                button.isEnabled = false
                binding.activeTasks.isChecked = false
                binding.activeTasks.isEnabled = true
                setupRecyclerViewByTasks(CompletedTasksRecyclerAdapter())
            }
        }
    }

    private fun setupRecyclerViewByTasks(specificAdapter: Adapter<*>) {
        val recyclerView: RecyclerView = binding.rvTasksList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = specificAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "TasksFragment"
    }
}