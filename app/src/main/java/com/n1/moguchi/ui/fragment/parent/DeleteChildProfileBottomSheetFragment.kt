package com.n1.moguchi.ui.fragment.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentDeleteChildBottomSheetBinding

class DeleteChildProfileBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentDeleteChildBottomSheetBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeleteChildBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deleteChildProfileBottomSheetFragment =
            view.findViewById<ConstraintLayout>(R.id.delete_child_profile_modal_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(deleteChildProfileBottomSheetFragment)
        bottomSheetBehavior.skipCollapsed = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        with(binding.bottomButtons) {
            leftButton.setOnClickListener {
                dismiss()
            }

            rightButton.text = getString(R.string.delete)
            rightButton.setOnClickListener {
                // TODO
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
