package com.n1.moguchi.ui.fragment.parent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentParentHomeBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.adapter.CompletedGoalsRecyclerAdapter
import com.n1.moguchi.ui.adapter.GoalsRecyclerAdapter
import com.n1.moguchi.ui.viewmodel.HomeViewModel
import javax.inject.Inject

class ParentHomeFragment : Fragment() {

    private var _binding: FragmentParentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var goalsRecyclerAdapter: GoalsRecyclerAdapter
    private lateinit var completedGoalsRecyclerAdapter: CompletedGoalsRecyclerAdapter

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
        _binding = FragmentParentHomeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(activity as MainActivity, R.id.fragment_container_view)

        val recyclerViewGoals: RecyclerView = view.findViewById(R.id.rv_home_goals_list)
        recyclerViewGoals.layoutManager = LinearLayoutManager(requireContext())
        goalsRecyclerAdapter = GoalsRecyclerAdapter()
        recyclerViewGoals.adapter = goalsRecyclerAdapter

        val recyclerViewCompletedGoals: RecyclerView = view.findViewById(R.id.rv_home_completed_goals_list)
        recyclerViewCompletedGoals.layoutManager = LinearLayoutManager(requireContext())
        completedGoalsRecyclerAdapter = CompletedGoalsRecyclerAdapter()
        recyclerViewCompletedGoals.adapter = completedGoalsRecyclerAdapter

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
//            viewModel.children.observe(viewLifecycleOwner) { childrenList ->
//                childrenList.map {
//                    val childItem = layoutInflater.inflate(
//                        R.layout.small_child_item,
//                        childrenLinearLayout,
//                        false
//                    )
//                    val childName = it.value.childName
//                    childItem.findViewById<TextView>(R.id.child_name).text = childName
//                    childrenLinearLayout.addView(childItem, 0)
//                }
//            }
        }

        binding.buttonAddChild.setOnClickListener {
            val bundle = bundleOf("isFromParentHome" to true)
            navController.navigate(R.id.action_parentHomeFragment_to_addChildFragment, bundle)
        }

        binding.homeAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    navController.navigate(R.id.action_parentHomeFragment_to_parentProfileFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}