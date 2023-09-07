package com.n1.moguchi.ui.fragments.parent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentHomeBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.adapters.GoalListAdapter
import com.n1.moguchi.ui.viewmodels.HomeViewModel
import javax.inject.Inject

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var goalListAdapter: GoalListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
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
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_home_goals_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        goalListAdapter = GoalListAdapter()
        recyclerView.adapter = goalListAdapter

        initFirstMockGoal(recyclerView)

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val parentId = Firebase.auth.currentUser?.uid

        val childrenLinearLayout = binding.childrenLinearLayout
        val mockSelectedChildItem = layoutInflater.inflate(
            R.layout.z_mock_selected_small_child_item,
            childrenLinearLayout,
            false
        )
        val mockChildItem = layoutInflater.inflate(
            R.layout.z_mock_small_child_item,
            childrenLinearLayout,
            false
        )
        var index = 0
        childrenLinearLayout.addView(mockSelectedChildItem, index++)
        childrenLinearLayout.addView(mockChildItem, index)

        if (parentId != null) {
            viewModel.getChildrenList(parentId)
            viewModel.children.observe(viewLifecycleOwner) { childrenList ->
                childrenList.map {
//                    val childItem = layoutInflater.inflate(
//                        R.layout.small_child_item,
//                        childrenLinearLayout,
//                        false
//                    )
//                    val childName = it.value.childName
//                    childItem.findViewById<TextView>(R.id.child_name).text = childName
//                    childrenLinearLayout.addView(childItem, 0)
                }
            }
        }

        binding.buttonAddChild.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_homeFragment_to_addChildFragment)
        }

        binding.homeAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    navController.navigate(R.id.action_homeFragment_to_profileFragment)
                    true
                }

                else -> false
            }
        }
    }

    private fun initFirstMockGoal(recyclerView: RecyclerView) {
        val initFirstGoal =
            layoutInflater.inflate(R.layout.z_mock_main_goal_card, recyclerView, false)
        goalListAdapter.goalsList.add(initFirstGoal)
    }
}
