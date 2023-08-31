package com.n1.moguchi.ui.fragments.parent

import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.commit
import androidx.fragment.app.replace
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

        childFragmentManager.commit {
            replace<ParentEditProfileFragment>(
                R.id.child_fragment_container,
                "ParentEditProfileFragment"
            )
            setReorderingAllowed(true)
        }

        binding.saveButton.setOnClickListener {
            TODO()
        }
    }
}