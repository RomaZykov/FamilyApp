package com.n1.moguchi.ui.fragment.switch_to_user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentSwitchToChildBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.adapter.ChildrenRecyclerAdapter
import javax.inject.Inject

class SwitchToUserBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSwitchToChildBinding? = null
    private val binding get() = _binding!!
    private lateinit var childrenRecyclerAdapter: ChildrenRecyclerAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SwitchToUserViewModel::class.java]
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
        super.onCreate(savedInstanceState)
        _binding = FragmentSwitchToChildBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parentID = Firebase.auth.currentUser?.uid

        val modalBottomSheet =
            view.findViewById<ConstraintLayout>(R.id.switch_to_child_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        if (parentID != null) {

            viewModel.getChildren(parentID)
            viewModel.children.observe(this) { children ->

                val recyclerView: RecyclerView = binding.rvChildrenList
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                childrenRecyclerAdapter = ChildrenRecyclerAdapter(children)
                recyclerView.adapter = childrenRecyclerAdapter
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}