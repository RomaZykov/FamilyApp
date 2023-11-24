package com.n1.moguchi.ui.fragments.parent.children_creation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.databinding.FragmentAddChildBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.adapters.ChildrenCreationRecyclerAdapter
import javax.inject.Inject

private const val ZERO_INDEX = 0

class AddChildFragment : Fragment() {
    private var _binding: FragmentAddChildBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var childrenCreationAdapter: ChildrenCreationRecyclerAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AddChildViewModel::class.java]
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
        _binding = FragmentAddChildBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        val parentId = auth.currentUser?.uid

        val isFromParentHome = arguments?.getBoolean("isFromParentHome")
        val isFromParentProfile = arguments?.getBoolean("isFromParentProfile")

        setupRecyclerView()
        viewModel.children.observe(viewLifecycleOwner) {
            if (parentId != null) {
                if (it.isEmpty()) {
                    addChild(parentId, ZERO_INDEX)
                }

                childrenCreationAdapter.onNewChildAddClicked = {
                    addChild(parentId, it.size)
                }

                childrenCreationAdapter.onChildRemoveClicked = { child ->
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

        if (isFromParentHome == true || isFromParentProfile == true) {
            binding.bottomBar.visibility = View.VISIBLE
            binding.topAppBar.visibility = View.VISIBLE
            if (isFromParentProfile == true) {
                binding.addChildAppBar.title = "Мои дети"
            }

            binding.nextButton.setOnClickListener {

            }
        }

    }

    private fun addChild(parentId: String?, index: Int) {
        if (parentId != null) {
            if (index == 0) {
                childrenCreationAdapter.children.add(
                    index,
                    viewModel.createNewChild(parentId, Child())
                )
            } else {
                childrenCreationAdapter.children.add(viewModel.createNewChild(parentId, Child()))
            }
            childrenCreationAdapter.notifyItemInserted(index)
            childrenCreationAdapter.notifyItemChanged(childrenCreationAdapter.itemCount - 1)
        } else {
            TODO()
        }
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.rvChildrenCreationList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        childrenCreationAdapter = ChildrenCreationRecyclerAdapter()
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