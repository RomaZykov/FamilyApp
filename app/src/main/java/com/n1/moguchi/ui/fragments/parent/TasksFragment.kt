package com.n1.moguchi.ui.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentTasksBinding
import com.n1.moguchi.ui.adapters.TasksListRecyclerAdapter

class TasksFragment : Fragment() {

    private lateinit var binding: FragmentTasksBinding
    private lateinit var tasksListRecyclerAdapter: TasksListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val recyclerView: RecyclerView = binding.rvTasksList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        tasksListRecyclerAdapter = TasksListRecyclerAdapter()
        recyclerView.adapter = tasksListRecyclerAdapter
    }
}