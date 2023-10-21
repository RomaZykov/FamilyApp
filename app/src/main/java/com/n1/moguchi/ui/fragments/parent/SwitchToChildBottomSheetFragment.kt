package com.n1.moguchi.ui.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentSwitchToChildBinding
import com.n1.moguchi.ui.ChildClickListener
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.adapters.ChildrenRecyclerAdapter

class SwitchToChildBottomSheetFragment : BottomSheetDialogFragment(), ChildClickListener {

    private var _binding: FragmentSwitchToChildBinding? = null
    private val binding get() = _binding!!
    private lateinit var childrenRecyclerAdapter: ChildrenRecyclerAdapter
    lateinit var listener: ChildClickListener

    private var mockNames: List<String> = listOf("Максимка", "Алёна")

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

        val modalBottomSheet = view.findViewById<ConstraintLayout>(R.id.switch_to_child_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        listener = this

        val recyclerView: RecyclerView = binding.rvChildrenList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        childrenRecyclerAdapter = ChildrenRecyclerAdapter(mockNames.toMutableList(), listener)
        recyclerView.adapter = childrenRecyclerAdapter

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onChildItemClick(view: View) {
        dismiss()
        (activity as MainActivity).navController.navigate(R.id.action_parentHomeFragment_to_onBoardingChildFragment)
    }
}