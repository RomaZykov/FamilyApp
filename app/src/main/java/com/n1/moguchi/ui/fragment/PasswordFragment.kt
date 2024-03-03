package com.n1.moguchi.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentPasswordBinding
import com.n1.moguchi.ui.fragment.parent.goal_creation.GoalCreationFragment

class PasswordFragment : Fragment() {

    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!
    private var isNextButtonPressed: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.passwordForChildEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(password: Editable?) {
                if (password.toString().isEmpty() && password.toString().isBlank()) {
                    binding.passwordForChildEditText.error = getString(R.string.password_error)
                    parentFragmentManager.clearFragmentResult("isButtonEnabledRequestKey")
                    parentFragmentManager.setFragmentResult(
                        "isButtonEnabledRequestKey",
                        bundleOf("buttonIsReadyKey" to false)
                    )
                } else {
                    parentFragmentManager.clearFragmentResult("isButtonEnabledRequestKey")
                    parentFragmentManager.setFragmentResult(
                        "isButtonEnabledRequestKey",
                        bundleOf("buttonIsReadyKey" to true)
                    )
                }
            }
        })

        parentFragmentManager.setFragmentResultListener(
            "nextButtonPressedRequestKey",
            viewLifecycleOwner
        ) { _, bundle ->
            isNextButtonPressed = bundle.getBoolean("buttonIsPressedKey")
            if (isNextButtonPressed == true) {
                val password = binding.passwordForChildEditText.text.toString().toInt()
                val currentChildID =
                    requireParentFragment().arguments?.getString(GoalCreationFragment.CHILD_ID_KEY)
                val newBundle = Bundle().apply {
                    this.putString(currentChildID, password.toString())
                }
                parentFragmentManager.setFragmentResult("childCreationProcessCompletedRequestKey", newBundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
