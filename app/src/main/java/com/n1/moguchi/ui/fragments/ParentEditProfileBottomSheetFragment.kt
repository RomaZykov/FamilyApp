package com.n1.moguchi.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.databinding.FragmentPrimaryBottomSheetBinding
import com.n1.moguchi.databinding.FragmentProfileEditBottomSheetBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.viewmodels.PrimaryBottomSheetViewModel
import javax.inject.Inject

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

        binding.saveButton.setOnClickListener {

        }
    }

    companion object {
        const val TAG = "ParentEditProfileModalBottomSheet"
    }
}