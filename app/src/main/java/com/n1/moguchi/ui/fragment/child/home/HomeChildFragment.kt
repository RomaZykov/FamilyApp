package com.n1.moguchi.ui.fragment.child.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentChildHomeBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.adapter.GoalsRecyclerAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeChildFragment : Fragment() {

    private var _binding: FragmentChildHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var childGoalsRecyclerAdapter: GoalsRecyclerAdapter
//    private lateinit var childCompletedGoalsRecyclerAdapter: CompletedGoalsRecyclerAdapter

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

        val navController =
            Navigation.findNavController(activity as MainActivity, R.id.fragment_container_view)

        val childId = requireArguments().getString("childId")
        if (childId != null) {
            lifecycleScope.launch {
                viewModel.getChild(childId).collectLatest {
                    binding.childHomeAppBar.menu.findItem(R.id.childProfile)
                        .setIcon(it?.imageResourceId!!)
                }
            }
            viewModel.fetchGoalsAndTasks(childId)
            viewModel.fetchCompletedGoals(childId)
        }


        viewModel.goals.observe(viewLifecycleOwner) {
            val recyclerViewGoals: RecyclerView =
                view.findViewById(R.id.rv_child_home_goals_list)
            recyclerViewGoals.layoutManager = LinearLayoutManager(requireContext())
            childGoalsRecyclerAdapter =
                GoalsRecyclerAdapter(it.keys.toList(), it.flatMap { map ->
                    map.value
                })
            recyclerViewGoals.adapter = childGoalsRecyclerAdapter

            childGoalsRecyclerAdapter.onGoalButtonClicked = { goalId ->
                val bundle = bundleOf("goalId" to goalId)
                navController.navigate(R.id.action_homeChildFragment_to_tasksFragment, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}