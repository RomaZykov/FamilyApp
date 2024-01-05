package com.n1.moguchi.ui.fragment.parent.home

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
import com.n1.moguchi.ui.adapter.ChildrenRecyclerAdapter
import com.n1.moguchi.ui.adapter.CompletedGoalsRecyclerAdapter
import com.n1.moguchi.ui.adapter.GoalsRecyclerAdapter
import javax.inject.Inject

class ParentHomeFragment : Fragment() {

    private var _binding: FragmentParentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var goalsRecyclerAdapter: GoalsRecyclerAdapter
    private lateinit var completedGoalsRecyclerAdapter: CompletedGoalsRecyclerAdapter
    private lateinit var childrenRecyclerAdapter: ChildrenRecyclerAdapter
    private var selectedChildIndex = 0
    private val childrenIdList = mutableListOf<String>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ParentHomeViewModel::class.java]
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
        val parentID = Firebase.auth.currentUser?.uid
        val navController =
            Navigation.findNavController(activity as MainActivity, R.id.fragment_container_view)

        if (parentID != null) {
            viewModel.getChildren(parentID)
            viewModel.children.observe(viewLifecycleOwner) { childrenList ->
                childrenList.forEach {
                    childrenIdList.add(it.key)
                }
                viewModel.fetchGoalsAndTasks(childrenIdList[selectedChildIndex])
                viewModel.getCompletedGoals(childrenIdList[selectedChildIndex])

                val childrenRecyclerView: RecyclerView = binding.rvChildren
                childrenRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                childrenRecyclerAdapter =
                    ChildrenRecyclerAdapter(childrenList.values.toList(), selectedChildIndex)
                childrenRecyclerView.adapter = childrenRecyclerAdapter
            }

            viewModel.goals.observe(viewLifecycleOwner) {
                val goalsRecyclerView: RecyclerView = binding.rvHomeGoalsList
                goalsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                goalsRecyclerAdapter = GoalsRecyclerAdapter(it.keys.toList(), it.flatMap { map ->
                    map.value
                })
                goalsRecyclerView.adapter = goalsRecyclerAdapter

                goalsRecyclerAdapter.onGoalButtonClicked = { goalId ->
                    val bundle = bundleOf("goalId" to goalId)
                    navController.navigate(R.id.action_parentHomeFragment_to_tasksFragment, bundle)
                }
            }

            viewModel.completedGoals.observe(viewLifecycleOwner) { completedGoals ->
                if (completedGoals.isEmpty()) {
                    binding.completedGoalsParentSide.root.visibility = View.GONE
                } else {
                    binding.completedGoalsParentSide.root.visibility = View.VISIBLE
                    val completedGoalsRecyclerView: RecyclerView =
                        binding.completedGoalsParentSide.rvHomeCompletedGoalsList
                    completedGoalsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    completedGoalsRecyclerAdapter = CompletedGoalsRecyclerAdapter(completedGoals)
                    completedGoalsRecyclerView.adapter = completedGoalsRecyclerAdapter
                }
            }

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