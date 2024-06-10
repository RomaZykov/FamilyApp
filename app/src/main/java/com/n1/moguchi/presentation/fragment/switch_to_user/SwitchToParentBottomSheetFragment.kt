package com.n1.moguchi.presentation.fragment.switch_to_user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.remote.model.ProfileMode
import com.n1.moguchi.databinding.FragmentSwitchToParentBottomSheetBinding
import com.n1.moguchi.presentation.ViewModelFactory
import com.n1.moguchi.presentation.activity.MainActivity
import com.n1.moguchi.presentation.fragment.child.home.HomeChildFragmentDirections
import kotlinx.coroutines.launch
import javax.inject.Inject

class SwitchToParentBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSwitchToParentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

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
        auth = Firebase.auth

        val navController = findNavController()
        showBottomSheet(view)

        val childId = requireArguments().getString("childId")

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (childId != null) {
                    viewModel.getChild(childId).collect {
                        binding.passwordToParent.forgotPassword.visibility = View.VISIBLE
                        binding.passwordToParent.forgotPassword.setOnClickListener {
                            Toast.makeText(
                                context,
                                getString(R.string.child_password_notification_by_email),
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.resetPassword(childId)
                        }
                    }
                }
            }
        }

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

                                viewModel.updateUserPrefs(ProfileMode.PARENT_MODE, null)
                                val action =
                                    HomeChildFragmentDirections.actionHomeChildFragmentToHomeParentFragment()
                                navController.navigate(action)
                                dismiss()
                            } else {
                                binding.passwordToParent.passwordForChildInputLayout.error =
                                    getString(
                                        R.string.not_correct_child_password
                                    )
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