package com.n1.moguchi.ui.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentSecondaryBottomSheetBinding

class SecondaryBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSecondaryBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondaryBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val secondaryModalBottomSheet =
            view.findViewById<ConstraintLayout>(R.id.secondary_modal_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(secondaryModalBottomSheet)
        bottomSheetBehavior.skipCollapsed = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.radioGroup1.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_every_day -> {
                    binding.divider1.visibility = View.GONE
                    binding.daysToRepeatTv.visibility = View.GONE
                    binding.weekdaysLl.visibility = View.GONE
                    binding.monthDaysLl.visibility = View.GONE
                }

                R.id.radio_per_week -> {
                    binding.divider1.visibility = View.VISIBLE
                    binding.daysToRepeatTv.visibility = View.VISIBLE
                    binding.weekdaysLl.visibility = View.VISIBLE
                    binding.monthDaysLl.visibility = View.GONE
                }

                R.id.radio_per_month -> {
                    binding.divider1.visibility = View.VISIBLE
                    binding.monthDaysLl.visibility = View.VISIBLE
                    binding.daysToRepeatTv.visibility = View.GONE
                    binding.weekdaysLl.visibility = View.GONE
                }
            }
        }

        binding.radioGroup2.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_never -> {
                    binding.repeatCounterEditText.visibility = View.GONE
                }

                R.id.radio_on_specific_date -> {
                    binding.repeatCounterEditText.visibility = View.GONE

                }

                R.id.radio_after_repeats -> {
                    binding.repeatCounterEditText.visibility = View.VISIBLE
                }
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

    companion object {
        const val TAG = "SecondaryBottomSheet"
    }
}
