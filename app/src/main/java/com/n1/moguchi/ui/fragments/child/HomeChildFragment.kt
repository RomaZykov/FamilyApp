package com.n1.moguchi.ui.fragments.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentChildHomeBinding
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.adapters.GoalsRecyclerAdapter
import com.n1.moguchi.ui.fragments.parent.ChangeProfileBottomSheetFragment

class HomeChildFragment : Fragment() {

    private lateinit var binding: FragmentChildHomeBinding
    private lateinit var goalsRecyclerAdapter: GoalsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChildHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.isParentProfile = false

        val recyclerViewGoals: RecyclerView = view.findViewById(R.id.rv_child_home_goals)
        recyclerViewGoals.layoutManager = LinearLayoutManager(requireContext())
        goalsRecyclerAdapter = GoalsRecyclerAdapter()
        recyclerViewGoals.adapter = goalsRecyclerAdapter

        updateBottomNavigationView()

        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view).menu.findItem(R.id.switchToParent).setOnMenuItemClickListener {
            showToParentChangeBottomSheet()
            true
        }
    }

    private fun showToParentChangeBottomSheet() {
        val fragmentManager = (activity as MainActivity).supportFragmentManager
        val modalBottomSheet = ChangeProfileBottomSheetFragment()
        val mainActivityTag = MainActivity.TAG
        modalBottomSheet.show(fragmentManager, mainActivityTag)
    }

    private fun updateBottomNavigationView() {
        (activity as MainActivity).setupBottomNavigationView()
    }
}