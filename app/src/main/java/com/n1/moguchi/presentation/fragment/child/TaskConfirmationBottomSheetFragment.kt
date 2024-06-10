package com.n1.moguchi.presentation.fragment.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentConfirmationTaskBottomSheetBinding
import com.ncorti.slidetoact.SlideToActView

class TaskConfirmationBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentConfirmationTaskBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmationTaskBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modalBottomSheet =
            view.findViewById<ConstraintLayout>(R.id.confirmation_task_modal_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.swipeableConfirmationButton.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                if (view.isCompleted()) {
                    dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}