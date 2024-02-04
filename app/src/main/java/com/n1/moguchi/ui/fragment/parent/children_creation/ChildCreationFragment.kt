package com.n1.moguchi.ui.fragment.parent.children_creation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.databinding.FragmentChildCreationBinding
import com.n1.moguchi.ui.ViewModelFactory
import javax.inject.Inject

class ChildCreationFragment(private val deleteChildOptionEnable: Boolean) : Fragment() {

    private var _binding: FragmentChildCreationBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var childrenCreationAdapter: ChildrenCreationRecyclerAdapter

    private var initChildrenRecyclerOnlyOnceFlag = true

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ChildCreationViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as MoguchiBaseApplication).appComponent
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChildCreationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val parentId = auth.currentUser?.uid

        val isFromParentProfile = arguments?.getBoolean("isFromParentProfile")
        if (isFromParentProfile == true) {
            binding.myChildrenTopAppBar.visibility = View.VISIBLE
            val topAppBar = requireActivity().findViewById<Toolbar>(R.id.childCreationAppBar)
            topAppBar.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
            binding.myChildrenBottomBar.visibility = View.VISIBLE
        }

        if (parentId != null) {
            viewModel.fetchChildren(parentId)
            setupRecyclerView()
            viewModel.children.observe(viewLifecycleOwner) { children ->
                if (children.isEmpty() && deleteChildOptionEnable) {
                    addChild(parentId, 0)
                }

                if (!deleteChildOptionEnable && initChildrenRecyclerOnlyOnceFlag) {
                    initChildrenRecyclerOnlyOnceFlag = false
                    childrenCreationAdapter.children = children.toMutableList()
                    childrenCreationAdapter.notifyItemRangeInserted(
                        0,
                        childrenCreationAdapter.children.size
                    )
                }

                childrenCreationAdapter.onNewChildAddClicked = {
                    if (childrenCreationAdapter.children.size == 1) {
                        childrenCreationAdapter.notifyItemChanged(0)
                    }
                    addChild(parentId, children.size)
                }

                childrenCreationAdapter.onChildRemoveClicked = { child, position ->
                    if (position == 0 && childrenCreationAdapter.children.size == 2) {
                        childrenCreationAdapter.notifyItemChanged(1)
                    }
                    if (position == 1 && childrenCreationAdapter.children.size == 2) {
                        childrenCreationAdapter.notifyItemChanged(0)
                    }
                    viewModel.deleteChildProfile(parentId, child.childId!!)
                }

                childrenCreationAdapter.onChildUpdate = { child ->
                    viewModel.onChildUpdate(parentId, child)
                }

                childrenCreationAdapter.onCardsStatusUpdate = { isAllCardsCompleted ->
                    parentFragmentManager.setFragmentResult(
                        "buttonIsEnabled",
                        bundleOf("buttonIsReadyKey" to isAllCardsCompleted)
                    )
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun addChild(parentId: String, childrenSize: Int) {
        if (childrenSize == 0) {
            childrenCreationAdapter.children.add(
                0,
                viewModel.createNewChild(parentId, Child())
            )
            childrenCreationAdapter.notifyItemInserted(0)
        } else {
            childrenCreationAdapter.children.add(viewModel.createNewChild(parentId, Child()))
            childrenCreationAdapter.notifyItemInserted(childrenSize)
        }
        childrenCreationAdapter.notifyItemChanged(childrenCreationAdapter.itemCount - 1)
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.rvChildrenCreationList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        childrenCreationAdapter = ChildrenCreationRecyclerAdapter(deleteChildOptionEnable)
        recyclerView.adapter = childrenCreationAdapter
        recyclerView.recycledViewPool.setMaxRecycledViews(
            ChildrenCreationRecyclerAdapter.VIEW_TYPE_CHILD_CARD,
            ChildrenCreationRecyclerAdapter.MAX_POOL_SIZE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
