package com.n1.moguchi.ui.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentCompletedTasksBinding
import com.n1.moguchi.ui.adapters.CompletedTasksRecyclerAdapter

class CompletedTasksFragment : Fragment() {

    private var _binding: FragmentCompletedTasksBinding? = null
    private val binding get() = _binding!!
    private lateinit var completedTasksRecyclerAdapter: CompletedTasksRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletedTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.rvTasksList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        completedTasksRecyclerAdapter = CompletedTasksRecyclerAdapter()
        recyclerView.adapter = completedTasksRecyclerAdapter

        val topAppBar = requireActivity().findViewById<Toolbar>(R.id.completed_tasks_app_bar)
        topAppBar.setNavigationOnClickListener {
            parentFragmentManager.commit {
                remove(this@CompletedTasksFragment)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}