package com.example.familyapp.ui.fragment.parent

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.familyapp.R
import com.example.familyapp.databinding.FragmentSecondaryBottomSheetBinding
import java.util.Calendar


class TaskSettingsSecondaryBottomSheetFragment : BottomSheetDialogFragment() {

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

        binding.radioGroup1.setOnCheckedChangeListener { _, checkedId ->
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

        binding.radioGroup2.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_never -> {
                    binding.repeatCounterEditText.visibility = View.GONE
                    binding.specificDateEditText.visibility = View.GONE
                }

                R.id.radio_on_specific_date -> {
                    binding.specificDateEditText.visibility = View.VISIBLE
                    binding.repeatCounterEditText.visibility = View.GONE
                    binding.datePickerButton.setOnClickListener {
                        val calendar = Calendar.getInstance()
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH)
                        val day = calendar.get(Calendar.DAY_OF_MONTH)
                        val datePicker =
                            DatePickerDialog(
                                requireContext(), R.style.Theme_DatePicker,
                                { datePicker, datePickerYear, monthOfYear, dayOfMonth ->
                                    TODO()
                                }, year, month, day
                            )
                        datePicker.show()
                    }
                }

                R.id.radio_after_repeats -> {
                    binding.specificDateEditText.visibility = View.GONE
                    binding.repeatCounterEditText.visibility = View.VISIBLE
                }
            }
        }

        binding.weekdaysLl.children.distinctBy {
            when (it.id) {
                R.id.monday -> {
                    TODO()
                }

                else -> {
                    TODO()
                }
            }
        }

        binding.addDate.setOnClickListener {
            val newDate =
                LayoutInflater.from(requireContext()).inflate(R.layout.add_date_item, null)
            val params = LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 16, 0, 16)
            binding.monthDaysLl.addView(newDate, 2, params)
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
