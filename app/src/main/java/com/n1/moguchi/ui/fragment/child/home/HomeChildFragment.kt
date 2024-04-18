package com.n1.moguchi.ui.fragment.child.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentChildHomeBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.adapter.CompletedGoalsRecyclerAdapter
import com.n1.moguchi.ui.adapter.GoalsRecyclerAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeChildFragment : Fragment() {

    private var _binding: FragmentChildHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var childGoalsRecyclerAdapter: GoalsRecyclerAdapter

    private lateinit var childCompletedGoalsRecyclerAdapter: CompletedGoalsRecyclerAdapter
    private var currentChildId: String? = null


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeChildViewModel::class.java]
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
        _binding = FragmentChildHomeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        if (arguments == null) {
            lifecycleScope.launch {
                viewModel.getUserPrefs().collect {
                    currentChildId = it.currentChildId
                }
            }
        } else {
            currentChildId = requireArguments().getString("childId")
        }
        (activity as MainActivity).intent.putExtras(childIdBundle(currentChildId!!))

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchChildData(currentChildId!!).collect {
                    binding.childHomeAppBar.menu.findItem(R.id.childProfile)
                        .setIcon(it.imageResourceId!!)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchActiveGoalsWithTasks(currentChildId!!)
            viewModel.fetchCompletedGoalsWithTasks(currentChildId!!)
            viewModel.activeGoalsWithTasks.collect {
                if (it.keys.isEmpty()) {
                    binding.activeGoalsNotFoundChild.visibility = View.VISIBLE
                } else {
                    binding.activeGoalsNotFoundChild.visibility = View.GONE
                }
                val activeGoalsRecycler: RecyclerView =
                    view.findViewById(R.id.rv_child_home_goals_list)
                activeGoalsRecycler.layoutManager = LinearLayoutManager(requireContext())
                childGoalsRecyclerAdapter =
                    GoalsRecyclerAdapter(it.keys.toMutableList(), it.flatMap { map ->
                        map.value
                    }.toMutableList())
                activeGoalsRecycler.adapter = childGoalsRecyclerAdapter

                childGoalsRecyclerAdapter.onTasksEditingClicked = { goalId ->
                    val bundle = bundleOf("goalId" to goalId, "goalCompleted" to false)
                    navController.navigate(
                        R.id.action_homeChildFragment_to_tasksFragment,
                        bundle
                    )
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.completedGoalsWithTasks.collect {
                if (it.keys.isNotEmpty()) {
                    binding.completedGoalsChildSide.root.visibility = View.VISIBLE
                } else {
                    binding.completedGoalsChildSide.root.visibility = View.GONE
                }
                val completedGoalsRecycler: RecyclerView =
                    binding.completedGoalsChildSide.rvHomeCompletedGoalsList
                completedGoalsRecycler.layoutManager = LinearLayoutManager(requireContext())
                childCompletedGoalsRecyclerAdapter = CompletedGoalsRecyclerAdapter(
                    it.keys.toMutableList()
                )
                completedGoalsRecycler.adapter = childCompletedGoalsRecyclerAdapter

                childCompletedGoalsRecyclerAdapter.onTasksHistoryClicked = { goalId ->
                    val bundle = bundleOf("goalId" to goalId, "goalCompleted" to true)
                    navController.navigate(
                        R.id.action_homeChildFragment_to_tasksFragment,
                        bundle
                    )
                }
            }
        }
    }

    private fun childIdBundle(childId: String): Bundle {
        val bundle = Bundle().apply {
            this.putString("childId", childId)
        }
        return bundle
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}