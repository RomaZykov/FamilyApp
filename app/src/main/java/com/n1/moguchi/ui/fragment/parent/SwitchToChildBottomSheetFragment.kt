package com.n1.moguchi.ui.fragment.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.databinding.FragmentSwitchToChildBinding
import com.n1.moguchi.ui.adapter.ChildrenRecyclerAdapter

class SwitchToChildBottomSheetFragment(private val children: List<Child>) : BottomSheetDialogFragment() {

    private var _binding: FragmentSwitchToChildBinding? = null
    private val binding get() = _binding!!
    private lateinit var childrenRecyclerAdapter: ChildrenRecyclerAdapter

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

            val recyclerView: RecyclerView = binding.rvChildrenList
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            childrenRecyclerAdapter = ChildrenRecyclerAdapter(children)
            recyclerView.adapter = childrenRecyclerAdapter
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