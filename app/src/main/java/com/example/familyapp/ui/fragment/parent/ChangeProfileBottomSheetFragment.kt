package com.example.familyapp.ui.fragment.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.familyapp.R
import com.example.familyapp.databinding.FragmentChangeProfileBottomSheetBinding

class ChangeProfileBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentChangeProfileBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeProfileBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val changeProfileBottomSheetFragment =
            view.findViewById<ConstraintLayout>(R.id.change_profile_modal_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(changeProfileBottomSheetFragment)
        bottomSheetBehavior.skipCollapsed = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.subTitle.visibility = View.VISIBLE
        binding.passwordAreaLl.visibility = View.VISIBLE

        binding.forgotPassword.setOnClickListener {
            binding.subTitle.text = getString(R.string.reset_password_mail_instructions)
            binding.passwordAreaLl.visibility = View.GONE
            binding.cancelButton.text = getString(R.string.congrats)
            binding.cancelButton.setTextColor(resources.getColor(R.color.white))
            binding.cancelButton.setBackgroundColor(resources.getColor(R.color.orange))
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
