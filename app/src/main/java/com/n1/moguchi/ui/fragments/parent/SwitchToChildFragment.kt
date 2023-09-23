package com.n1.moguchi.ui.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.databinding.FragmentSwitchToChildBinding
import com.n1.moguchi.ui.adapters.ChildrenRecyclerAdapter

class SwitchToChildFragment : Fragment() {

    private lateinit var binding: FragmentSwitchToChildBinding
    private lateinit var childrenRecyclerAdapter: ChildrenRecyclerAdapter
    private var mockNames: List<String> = listOf("Максимка", "Алена")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentSwitchToChildBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.rvChildrenList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        childrenRecyclerAdapter = ChildrenRecyclerAdapter(mockNames.toMutableList())
        recyclerView.adapter = childrenRecyclerAdapter
    }
}