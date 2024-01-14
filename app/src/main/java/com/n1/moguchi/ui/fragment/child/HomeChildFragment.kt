package com.n1.moguchi.ui.fragment.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentChildHomeBinding
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.adapter.CompletedGoalsRecyclerAdapter
import com.n1.moguchi.ui.adapter.GoalsRecyclerAdapter
import com.n1.moguchi.ui.fragment.parent.ChangeProfileBottomSheetFragment

class HomeChildFragment : Fragment() {

    private var _binding: FragmentChildHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var goalsRecyclerAdapter: GoalsRecyclerAdapter
    private lateinit var childCompletedGoalsRecyclerAdapter: CompletedGoalsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChildHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.isParentProfile = false

//        val recyclerViewCompletedGoals: RecyclerView = view.findViewById(R.id.rv_child_home_completed_goals)
//        recyclerViewCompletedGoals.layoutManager = LinearLayoutManager(requireContext())
//        childCompletedGoalsRecyclerAdapter = CompletedGoalsRecyclerAdapter()
//        recyclerViewCompletedGoals.adapter = childCompletedGoalsRecyclerAdapter

//        updateBottomNavigationView()

        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view).menu.findItem(R.id.switchToParent).setOnMenuItemClickListener {
            showToParentChangeBottomSheet()
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToParentChangeBottomSheet() {
        val fragmentManager = (activity as MainActivity).supportFragmentManager
        val modalBottomSheet = ChangeProfileBottomSheetFragment()
        val mainActivityTag = MainActivity.SWITCH_TO_USER_INTENT_TAG
        modalBottomSheet.show(fragmentManager, mainActivityTag)
    }

//    private fun updateBottomNavigationView() {
//        (activity as MainActivity).setupBottomNavigationView(children)
//    }
}