package com.example.familyapp.ui.fragment.parent.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.familyapp.FamilyAppBaseApplication
import com.example.familyapp.R
import com.example.familyapp.data.remote.model.Child
import com.example.familyapp.data.remote.model.Goal
import com.example.familyapp.data.remote.model.Task
import com.example.familyapp.databinding.FragmentParentHomeBinding
import com.example.familyapp.ui.ViewModelFactory
import com.example.familyapp.ui.adapter.ChildrenRecyclerAdapter
import com.example.familyapp.ui.adapter.CompletedGoalsRecyclerAdapter
import com.example.familyapp.ui.adapter.GoalsRecyclerAdapter
import com.example.familyapp.ui.fragment.parent.PrimaryContainerBottomSheetFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeParentFragment : Fragment() {

    private var _binding: FragmentParentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var goalsRecyclerAdapter: GoalsRecyclerAdapter
    private lateinit var completedGoalsRecyclerAdapter: CompletedGoalsRecyclerAdapter
    private lateinit var childrenRecyclerAdapter: ChildrenRecyclerAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeParentViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as FamilyAppBaseApplication).appComponent
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
        val navController = findNavController()

        val parentId = Firebase.auth.currentUser?.uid
        if (parentId != null) {
            val menuItem = binding.parentHomeAppBar.menu.findItem(R.id.parentProfile)
            val photoUrl = Firebase.auth.currentUser?.photoUrl.toString()
            viewModel.load(photoUrl, menuItem)

            viewModel.fetchChildren(parentId)
            viewModel.children.observe(viewLifecycleOwner) { childrenMap ->
                setupChildrenRecyclerView(childrenMap)

                childrenRecyclerAdapter.onChildClicked = { _, child ->
                    if (child != null) {
                        viewModel.selectAnotherChild(child)
                    }
                }

                childrenRecyclerAdapter.onAddChildClicked = {
                    showBottomSheet(PrimaryContainerBottomSheetFragment(), CHILD_CREATION_TAG)
                    childFragmentManager.setFragmentResultListener(
                        "refreshRecyclerViewRequestKey",
                        viewLifecycleOwner
                    ) { _, innerBundle ->
                        val addedChildren =
                            innerBundle.getParcelableArrayList<Child>("children")?.toList()
                        if (addedChildren != null) {
                            viewModel.fetchChildren(parentId)
                            childrenRecyclerAdapter.updateChildrenList =
                                addedChildren.toMutableList()
                            childrenRecyclerAdapter.notifyItemRangeInserted(
                                childrenMap.size,
                                addedChildren.size
                            )
                        }
                    }
                }
            }

            viewModel.selectedChild.observe(viewLifecycleOwner) { child ->
                if (child != null) {
                    viewModel.fetchActiveGoalsWithTasks(child.childId!!)
                    viewModel.fetchCompletedGoalsWithTasks(child.childId)
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.activeGoalsWithTasks.collect {
                        if (it.keys.isEmpty()) {
                            binding.activeGoalsNotFound.visibility = View.VISIBLE
                        } else {
                            binding.activeGoalsNotFound.visibility = View.GONE
                        }
                        val goalsRecyclerView: RecyclerView = binding.rvHomeGoalsList
                        goalsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                        goalsRecyclerAdapter =
                            GoalsRecyclerAdapter(
                                it.keys.toMutableList(),
                                it.flatMap { map ->
                                    map.value
                                }.toMutableList()
                            )
                        goalsRecyclerView.adapter = goalsRecyclerAdapter

                        goalsRecyclerAdapter.onTasksEditingClicked = { goalId ->
                            val bundle = bundleOf("goalId" to goalId,  "goalCompleted" to false)
                            navController.navigate(
                                R.id.action_parentHomeFragment_to_tasksFragment,
                                bundle
                            )
                        }
                    }
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.completedGoalsWithTasks.collect {
                        if (it.keys.isNotEmpty()) {
                            binding.completedGoalsParentSide.root.visibility = View.VISIBLE
                        } else {
                            binding.completedGoalsParentSide.root.visibility = View.GONE
                        }
                        val completedGoalsRecycleView: RecyclerView =
                            binding.completedGoalsParentSide.rvHomeCompletedGoalsList
                        completedGoalsRecycleView.layoutManager =
                            LinearLayoutManager(requireContext())
                        completedGoalsRecyclerAdapter = CompletedGoalsRecyclerAdapter(
                            it.keys.toMutableList()
                        )
                        completedGoalsRecycleView.adapter = completedGoalsRecyclerAdapter

                        completedGoalsRecyclerAdapter.onTasksHistoryClicked = { goalId ->
                            val bundle = bundleOf("goalId" to goalId, "goalCompleted" to true)
                            navController.navigate(
                                R.id.action_parentHomeFragment_to_tasksFragment,
                                bundle
                            )
                        }
                    }
                }
            }
        }

        binding.parentHomeAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.parentProfile -> {
                    val bundle = bundleOf(PARENT_ID_KEY to parentId)
                    navController.navigate(
                        R.id.action_parentHomeFragment_to_parentProfileFragment,
                        bundle
                    )
                    true
                }

                else -> false
            }
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            "refreshGoalsListRequestKey",
            viewLifecycleOwner
        )
        { _, innerBundle ->
            val goal = innerBundle.getParcelable<Goal>("goal")
            val tasks = innerBundle.getParcelableArrayList<Task>("tasks")
            if (goal != null && tasks != null) {
                goalsRecyclerAdapter.updatedGoal = goal
                goalsRecyclerAdapter.updatedTasksList = tasks
                goalsRecyclerAdapter.notifyItemInserted(
                    childrenRecyclerAdapter.itemCount
                )
            }
        }
    }

    private fun setupChildrenRecyclerView(childrenMap: Map<String, Child>) {
        val childrenRecyclerView: RecyclerView = binding.rvChildren
        childrenRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        childrenRecyclerAdapter =
            ChildrenRecyclerAdapter(childrenMap.values.toMutableList(), 0)
        childrenRecyclerView.adapter = childrenRecyclerAdapter
    }

    private fun showBottomSheet(bottomSheetFragment: BottomSheetDialogFragment, tag: String) {
        childFragmentManager.setFragmentResult(
            "primaryBottomSheetRequestKey",
            bundleOf("primaryBundleKey" to tag)
        )
        bottomSheetFragment.show(
            childFragmentManager,
            tag
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CHILD_CREATION_TAG = "ChildCreationIntent"
        const val PARENT_ID_KEY = "parentIdKey"
    }
}