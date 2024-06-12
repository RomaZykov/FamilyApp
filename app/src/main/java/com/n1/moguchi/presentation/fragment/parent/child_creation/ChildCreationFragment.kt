package com.n1.moguchi.presentation.fragment.parent.child_creation

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.remote.model.Child
import com.n1.moguchi.databinding.FragmentChildCreationBinding
import com.n1.moguchi.presentation.ViewModelFactory
import com.n1.moguchi.presentation.fragment.parent.DeleteChildProfileBottomSheetFragment
import javax.inject.Inject

class ChildCreationFragment : Fragment() {

    private var _binding: FragmentChildCreationBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var childrenCreationAdapter: ChildrenCreationRecyclerAdapter

    private var deleteChildOptionEnable: Boolean = false
    private var isFromParentProfile: Boolean = false
    private var isFromParentHome: Boolean = false
    private var isFromOnBoarding: Boolean = false

    private var childrenForParse: MutableList<Child> = mutableListOf()

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
            setupRecyclerView()
            if (isFromParentHome || !isFromParentProfile) {
                addChildCardToRecyclerList(parentId, 0)
            } else {
                viewModel.fetchChildren(parentId)
                viewModel.children.observeOnce(viewLifecycleOwner) {
                    childrenCreationAdapter.children = it.toMutableList()
                    childrenCreationAdapter.notifyItemRangeInserted(
                        0,
                        childrenCreationAdapter.children.size
                    )
                }
            }

            viewModel.children.observe(viewLifecycleOwner) { children ->
                childrenCreationAdapter.onNewChildAddClicked = {
                    if (childrenCreationAdapter.children.size == 1) {
                        childrenCreationAdapter.notifyItemChanged(0)
                    }
                    addChildCardToRecyclerList(parentId, children.size)
                }

                childrenCreationAdapter.onChildRemoveClicked = { child, position ->
                    if (position == 0 && childrenCreationAdapter.children.size == 2) {
                        childrenCreationAdapter.notifyItemChanged(1)
                    }
                    if (position == 1 && childrenCreationAdapter.children.size == 2) {
                        childrenCreationAdapter.notifyItemChanged(0)
                    }
                    childrenForParse.removeAt(position)
                }

                childrenCreationAdapter.onChildRemoveViaBottomSheetClicked = { child, position ->
                    showBottomSheet(TO_DELETE_CHILD_PROFILE)
                    childFragmentManager.setFragmentResultListener(
                        "deleteChildProfileClickedRequestKey",
                        this
                    ) { _, _ ->
                        if (isFromParentProfile) {
                            viewModel.deleteChildProfile(child.childId!!)
                            childrenCreationAdapter.children.removeAt(position)
                            childrenCreationAdapter.notifyItemRemoved(position)
                        }
                    }
                }

                childrenCreationAdapter.onCardsStatusUpdate = { isAllCardsCompleted ->
                    parentFragmentManager.setFragmentResult(
                        "isButtonEnabledRequestKey",
                        bundleOf("buttonIsReadyKey" to isAllCardsCompleted)
                    )
                }
            }
        }

        parentFragmentManager.setFragmentResultListener(
            "nextButtonPressedRequestKey",
            viewLifecycleOwner
        ) { _, bundle ->
            val nextButtonPressed = bundle.getBoolean("buttonIsPressedKey")
            if (nextButtonPressed) {
                val newBundle = Bundle().apply {
                    this.putParcelableArrayList(
                        "children",
                        childrenForParse as ArrayList<out Parcelable>
                    )
                }
                parentFragmentManager.setFragmentResult("createBundleRequestKey", newBundle)
            }
        }

        binding.cancelButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun addChildCardToRecyclerList(parentId: String, childrenSize: Int) {
        val createdChild = viewModel.returnCreatedChild(parentId, Child())
        childrenForParse.add(createdChild)
        if (childrenSize == 0) {
            childrenCreationAdapter.children.add(
                0,
                createdChild
            )
            childrenCreationAdapter.notifyItemInserted(0)
        } else {
            childrenCreationAdapter.children.add(createdChild)
            childrenCreationAdapter.notifyItemInserted(childrenSize)
        }
        childrenCreationAdapter.notifyItemChanged(childrenCreationAdapter.itemCount - 1)
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.rvChildrenCreationList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        childrenCreationAdapter = when {
            isFromOnBoarding -> {
                ChildrenCreationRecyclerAdapter(
                    deleteChildOptionEnable,
                    addChildButtonEnable = true,
                    removeChildFastProcessEnable = true,
                    passwordEnable = false
                )
            }

            isFromParentHome -> {
                ChildrenCreationRecyclerAdapter(
                    deleteChildOptionEnable,
                    addChildButtonEnable = true,
                    removeChildFastProcessEnable = true,
                    passwordEnable = true
                )
            }

            isFromParentProfile -> {
                ChildrenCreationRecyclerAdapter(
                    deleteChildOptionEnable,
                    addChildButtonEnable = false,
                    removeChildFastProcessEnable = false,
                    passwordEnable = false
                )
            }

            else -> {
                ChildrenCreationRecyclerAdapter(deleteChildOptionEnable)
            }
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
        isFromParentHome = args.getBoolean(FROM_PARENT_HOME)
        isFromOnBoarding = args.getBoolean(FROM_ON_BOARDING)
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

    companion object {
        private const val DELETE_CHILD_OPTION_ENABLE = "deleteChildOptionEnable"
        private const val FROM_PARENT_PROFILE = "isFromParentProfile"
        private const val FROM_ON_BOARDING = "isFromOnBoarding"
        private const val FROM_PARENT_HOME = "isFromParentHome"
        private const val TO_DELETE_CHILD_PROFILE = "deleteChildProfile"

        fun newInstance(
            isFromOnBoarding: Boolean,
            isFromParentHome: Boolean,
            isFromParentProfile: Boolean,
            deleteChildOptionEnable: Boolean
        ): ChildCreationFragment {
            return ChildCreationFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(FROM_ON_BOARDING, isFromOnBoarding)
                    putBoolean(FROM_PARENT_PROFILE, isFromParentProfile)
                    putBoolean(FROM_PARENT_HOME, isFromParentHome)
                    putBoolean(DELETE_CHILD_OPTION_ENABLE, deleteChildOptionEnable)
                }
            }
        }
    }
}

