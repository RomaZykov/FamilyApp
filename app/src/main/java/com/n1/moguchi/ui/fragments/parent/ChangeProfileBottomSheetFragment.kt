package com.n1.moguchi.ui.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentChangeProfileBottomSheetBinding

class ChangeProfileBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentChangeProfileBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeProfileBottomSheetBinding.inflate(inflater, container, false)
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
            binding.subTitle.text = "На вашу почту mail@mail.ru были отправлены дальнейшие инструкции по восстановлению пароля"
            binding.passwordAreaLl.visibility = View.GONE
            binding.cancelButton.text = "Отлично"
            binding.cancelButton.setTextColor(resources.getColor(R.color.white))
            binding.cancelButton.setBackgroundColor(resources.getColor(R.color.orange))
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        const val TAG = "ChangeProfileBottomSheet"
    }
}
