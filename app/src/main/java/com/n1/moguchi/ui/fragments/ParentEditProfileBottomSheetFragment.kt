package com.n1.moguchi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentProfileEditBottomSheetBinding

class ParentEditProfileBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentProfileEditBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileEditBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modalBottomSheet =
            view.findViewById<ConstraintLayout>(R.id.parent_edit_modal_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
        bottomSheetBehavior.skipCollapsed = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        childFragmentManager.beginTransaction()
            .replace(
                R.id.child_fragment_container, ParentEditProfileFragment()
            )
            .commit()

//        if () {
//            binding.bottomLinearLayout.visibility = View.GONE
//        }

        binding.saveButton.setOnClickListener {

        }
    }

    companion object {
        const val TAG = "ParentEditProfileModalBottomSheet"
        const val PARENT_EDIT_TAG = "ParentEditProfileFragment"
        const val PARENT_DELETE_TAG = "ParentDeleteProfileFragment"
    }
}