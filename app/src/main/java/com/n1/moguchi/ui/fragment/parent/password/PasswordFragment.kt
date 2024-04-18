package com.n1.moguchi.ui.fragment.parent.password

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.databinding.FragmentPasswordBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.fragment.parent.goal_creation.GoalCreationFragment
import javax.inject.Inject

class PasswordFragment : Fragment() {

    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!
    private var isNextButtonPressed: Boolean? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PasswordViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as MoguchiBaseApplication).appComponent
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

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
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(password: Editable?) {
                if (password.toString().isEmpty() && password.toString().isBlank()) {
                    binding.passwordForChildEditText.error = "Нужно установить код"
                    parentFragmentManager.clearFragmentResult("buttonIsEnabled")
                    parentFragmentManager.setFragmentResult(
                        "buttonIsEnabled",
                        bundleOf("buttonIsReadyKey" to false)
                    )
                } else {
                    parentFragmentManager.clearFragmentResult("buttonIsEnabled")
                    parentFragmentManager.setFragmentResult(
                        "buttonIsEnabled",
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
                viewModel.setPassword(password, currentChildID!!)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
