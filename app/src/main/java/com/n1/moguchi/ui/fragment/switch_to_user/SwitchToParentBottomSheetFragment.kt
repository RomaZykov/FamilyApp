package com.n1.moguchi.ui.fragment.switch_to_user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentSwitchToParentBottomSheetBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.activity.MainActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

class SwitchToParentBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSwitchToParentBottomSheetBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SwitchToUserViewModel::class.java]
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
        super.onCreate(savedInstanceState)
        _binding =
            FragmentSwitchToParentBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController =
            Navigation.findNavController(activity as MainActivity, R.id.fragment_container_view)
        showBottomSheet(view)

        val childId = requireArguments().getString("childId")

        binding.passwordToParent.forgotPassword.visibility = View.VISIBLE
        binding.passwordToParent.passwordDescription.text =
            getString(R.string.switch_to_parent_description)
        binding.passwordToParent.passwordForChildEditText.setOnEditorActionListener { password, actionId, _ ->
            var operationSuccess = false
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    if (childId != null) {
                        viewModel.checkPassword(
                            password = password.text.toString().toInt(),
                            childId = childId
                        ).collect { passwordCorrect ->
                            if (passwordCorrect && actionId == EditorInfo.IME_ACTION_DONE) {
                                operationSuccess = true
                                (requireActivity() as MainActivity).supportFragmentManager.setFragmentResult(
                                    "changeProfileModeRequestKey",
                                    bundleOf()
                                )
                                navController.navigate(
                                    R.id.action_homeChildFragment_to_homeParentFragment
                                )
                                dismiss()
                            }
                        }
                    }
                }
            }
            operationSuccess
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showBottomSheet(view: View) {
        val modalBottomSheet =
            view.findViewById<ConstraintLayout>(R.id.switch_to_parent_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}