package com.n1.moguchi.ui.fragment.parent.child_creation

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.databinding.FragmentChildCreationBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.fragment.parent.DeleteChildProfileBottomSheetFragment
import javax.inject.Inject

class ChildCreationFragment : Fragment() {

    private var _binding: FragmentChildCreationBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var childrenCreationAdapter: ChildrenCreationRecyclerAdapter

    private var initChildrenRecyclerOnlyOnceFlag: Boolean = true
    private var deleteChildOptionEnable: Boolean = false
    private var isFromParentProfile: Boolean = false

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

        parseParams()

        if (isFromParentProfile) {
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

                if (initChildrenRecyclerOnlyOnceFlag) {
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
                    viewModel.deleteChildProfile(child.childId!!)
                }

                childrenCreationAdapter.onChildRemoveForBottomSheetClicked = { child, position ->
                    showBottomSheet(TO_DELETE_CHILD_PROFILE)
                    childFragmentManager.setFragmentResultListener(
                        "deleteChildProfileClickedRequestKey",
                        this
                    ) { _, _ ->
                        childrenCreationAdapter.children.removeAt(position)
                        childrenCreationAdapter.notifyItemRemoved(position)
                        viewModel.deleteChildProfile(child.childId!!)
                    }
                }

                childrenCreationAdapter.onChildUpdate = { child ->
                    viewModel.onChildUpdate(parentId, child)
                }

                childrenCreationAdapter.onCardsStatusUpdate = { isAllCardsCompleted ->
                    parentFragmentManager.setFragmentResult(
                        "isButtonEnabledRequestKey",
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
        childrenCreationAdapter = if (isFromParentProfile) {
            ChildrenCreationRecyclerAdapter(deleteChildOptionEnable, isFromParentProfile)
        } else {
            ChildrenCreationRecyclerAdapter(deleteChildOptionEnable)
        }
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

    private fun parseParams() {
        val args = requireArguments()
        deleteChildOptionEnable = args.getBoolean(DELETE_CHILD_OPTION_ENABLE)
        isFromParentProfile = args.getBoolean(FROM_PARENT_PROFILE)
    }

    private fun showBottomSheet(tag: String) {
        val fragmentManager = childFragmentManager
        fragmentManager.setFragmentResult(
            "profileBottomSheetRequestKey",
            bundleOf("profileBundleKey" to tag)
        )
        val modalBottomSheet = DeleteChildProfileBottomSheetFragment() as BottomSheetDialogFragment
        modalBottomSheet.show(fragmentManager, null)
    }

    companion object {
        private const val DELETE_CHILD_OPTION_ENABLE = "deleteChildOptionEnable"
        private const val FROM_PARENT_PROFILE = "isFromParentProfile"
        private const val TO_DELETE_CHILD_PROFILE = "deleteChildProfile"

        fun newInstance(deleteChildOptionEnable: Boolean): ChildCreationFragment {
            return ChildCreationFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(DELETE_CHILD_OPTION_ENABLE, deleteChildOptionEnable)
                }
            }
        }
    }
}
