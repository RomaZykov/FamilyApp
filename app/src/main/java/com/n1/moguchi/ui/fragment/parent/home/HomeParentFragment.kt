package com.n1.moguchi.ui.fragment.parent.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.models.remote.Child
import com.n1.moguchi.data.models.remote.Goal
import com.n1.moguchi.data.models.remote.Task
import com.n1.moguchi.databinding.FragmentParentHomeBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.adapter.ChildrenRecyclerAdapter
import com.n1.moguchi.ui.adapter.CompletedGoalsRecyclerAdapter
import com.n1.moguchi.ui.adapter.GoalsRecyclerAdapter
import com.n1.moguchi.ui.fragment.parent.PrimaryContainerBottomSheetFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeParentFragment : Fragment() {

    private var _binding: FragmentParentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var goalsRecyclerAdapter: GoalsRecyclerAdapter
    private lateinit var completedGoalsRecyclerAdapter: CompletedGoalsRecyclerAdapter
    private lateinit var childrenRecyclerAdapter: ChildrenRecyclerAdapter
    private val childrenIdList = mutableListOf<String>()
    private var selectedChildIndex = 0

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeParentViewModel::class.java]
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
        val navController = findNavController()

        val parentId = Firebase.auth.currentUser?.uid
        if (parentId != null) {
            viewModel.fetchChildren(parentId)

            val menuItem = binding.parentHomeAppBar.menu.findItem(R.id.parentProfile)
            val photoUrl = Firebase.auth.currentUser?.photoUrl.toString()
            viewModel.load(photoUrl, menuItem)

            viewModel.children.observeOnce(viewLifecycleOwner) { childrenMap ->
                childrenMap.forEach {
                    childrenIdList.add(it.key)
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.fetchActiveGoalsWithTasks(childrenIdList[selectedChildIndex])
                            .collectLatest {
                                Log.d("HomeParentFragment", "childrenIdList = $childrenIdList, index = $selectedChildIndex")
                                val goalsRecyclerView: RecyclerView = binding.rvHomeGoalsList
                                goalsRecyclerView.layoutManager =
                                    LinearLayoutManager(requireContext())
                                goalsRecyclerAdapter =
                                    GoalsRecyclerAdapter(
                                        it.keys.toMutableList(),
                                        it.flatMap { map ->
                                            map.value
                                        }.toMutableList()
                                    )
                                goalsRecyclerView.adapter = goalsRecyclerAdapter

                                goalsRecyclerAdapter.onGoalButtonClicked = { goalId ->
                                    val bundle = bundleOf("goalId" to goalId)
                                    navController.navigate(
                                        R.id.action_parentHomeFragment_to_tasksFragment,
                                        bundle
                                    )
                                }
                            }

                        viewModel.fetchCompletedGoals(childrenIdList[selectedChildIndex])
                            .collect { completedGoals ->
                                if (completedGoals.isEmpty()) {
                                    binding.completedGoalsParentSide.root.visibility = View.GONE
                                } else {
                                    binding.completedGoalsParentSide.root.visibility = View.VISIBLE

                                    val completedGoalsRecyclerView: RecyclerView =
                                        binding.completedGoalsParentSide.rvHomeCompletedGoalsList
                                    completedGoalsRecyclerView.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    completedGoalsRecyclerAdapter =
                                        CompletedGoalsRecyclerAdapter(completedGoals)
                                    completedGoalsRecyclerView.adapter =
                                        completedGoalsRecyclerAdapter
                                }
                            }
                    }
                }
            }

            viewModel.children.observe(viewLifecycleOwner) { childrenMap ->
                val childrenRecyclerView: RecyclerView = binding.rvChildren
                childrenRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                childrenRecyclerAdapter =
                    ChildrenRecyclerAdapter(childrenMap.values.toMutableList(), selectedChildIndex)
                childrenRecyclerView.adapter = childrenRecyclerAdapter

                childrenRecyclerAdapter.onChildClicked = { childIndex, _ ->
                    selectedChildIndex = childIndex
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.fetchActiveGoalsWithTasks(childrenIdList[selectedChildIndex])
                            .collectLatest {
                                Log.d("HomeParentFragment", "childrenIdList = $childrenIdList, index = $selectedChildIndex")
                                goalsRecyclerAdapter =
                                    GoalsRecyclerAdapter(
                                        it.keys.toMutableList(),
                                        it.flatMap { map ->
                                            map.value
                                        }.toMutableList()
                                    )

                                goalsRecyclerAdapter.onGoalButtonClicked = { goalId ->
                                    val bundle = bundleOf("goalId" to goalId)
                                    navController.navigate(
                                        R.id.action_parentHomeFragment_to_tasksFragment,
                                        bundle
                                    )
                                }
                            }

                        viewModel.fetchCompletedGoals(childrenIdList[selectedChildIndex])
                            .collect { completedGoals ->
                                if (completedGoals.isEmpty()) {
                                    binding.completedGoalsParentSide.root.visibility = View.GONE
                                } else {
                                    binding.completedGoalsParentSide.root.visibility =
                                        View.VISIBLE

//                                        val completedGoalsRecyclerView: RecyclerView =
//                                            binding.completedGoalsParentSide.rvHomeCompletedGoalsList
//                                        completedGoalsRecyclerView.layoutManager =
//                                            LinearLayoutManager(requireContext())
//                                        completedGoalsRecyclerAdapter =
//                                            CompletedGoalsRecyclerAdapter(completedGoals)
//                                        completedGoalsRecyclerView.adapter =
//                                            completedGoalsRecyclerAdapter
                                }
                            }
                    }
                }

                childrenRecyclerAdapter.onChildAddClicked = {
                    showBottomSheet(PrimaryContainerBottomSheetFragment(), CHILD_CREATION_TAG)
                    childFragmentManager.setFragmentResultListener(
                        "refreshRecyclerViewRequestKey",
                        viewLifecycleOwner
                    ) { _, innerBundle ->
                        val addedChildren =
                            innerBundle.getParcelableArrayList<Child>("children")
                                ?.toList()
                        if (addedChildren != null) {
                            addedChildren.forEach {
                                childrenIdList.add(it.childId!!)
                            }
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
        ) { _, innerBundle ->
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

    // TODO - Learn more about this solution
    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(
            lifecycleOwner,
            object : Observer<T> {
                override fun onChanged(value: T) {
                    observer.onChanged(value)
                    removeObserver(this)
                }
            }
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