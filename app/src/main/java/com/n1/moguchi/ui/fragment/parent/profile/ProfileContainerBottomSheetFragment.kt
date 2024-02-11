package com.n1.moguchi.ui.fragment.parent.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentProfileBottomSheetBinding

class ProfileContainerBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentProfileBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var signInClient: SignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInClient = Identity.getSignInClient(requireContext())
        auth = Firebase.auth

        val navHostFragment = requireActivity()
            .supportFragmentManager
            .findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        setupBottomSheet(view)

        setFragmentResultListener(
            "profileBottomSheetRequestKey"
        ) { _, bundle ->
            Log.d("ProfileFragment", "Bundle 2 = $parentFragmentManager.")
            when (bundle.getString("profileBundleKey")) {
                "EditProfileIntent" -> {
                    binding.editProfileTitle.text = getString(R.string.edit)
                    startTransition(EditParentProfileFragment(), EDIT_PROFILE_TAG)
                }

                "LogOutIntent" -> {
                    binding.editProfileTitle.text = getString(R.string.log_out)
                    startTransition(LogOutFragment(), LOG_OUT_PROFILE_TAG)
                }
            }
        }

        with(binding.bottomButtons) {
            childFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
                when (fragment.tag) {
                    EDIT_PROFILE_TAG -> {
                        fragmentManager.setFragmentResultListener(
                            "deleteAccountPressedRequestKey",
                            viewLifecycleOwner
                        ) { _, innerBundle ->
                            val isButtonPressed = innerBundle.getBoolean("buttonIsPressedKey")
                            if (isButtonPressed) {
                                fragmentManager.commit {
                                    setReorderingAllowed(true)
                                    remove(fragment)
                                    replace(
                                        R.id.profile_child_fragment_container,
                                        DeleteProfileParentFragment(),
                                        PROFILE_DELETE_TAG
                                    )
                                }
                            }
                        }
                        with(leftButton) {
                            text = getString(R.string.cancel)
                            setOnClickListener {
                                dismiss()
                            }
                        }
                        with(rightButton) {
                            text = getString(R.string.save)
                            setOnClickListener {

                            }
                        }
                    }

                    LOG_OUT_PROFILE_TAG -> {
                        with(leftButton) {
                            text = getString(R.string.cancel)
                            setOnClickListener {
                                dismiss()
                            }
                        }
                        with(rightButton) {
                            text = getString(R.string.yes)
                            setOnClickListener {
                                auth.signOut()
                                signInClient.signOut().addOnCompleteListener {
                                    dismiss()
                                    navController.navigate(R.id.action_parentProfileFragment_to_registrationFragment)
                                }
                            }
                        }
                    }

                    PROFILE_DELETE_TAG -> {
                        binding.editProfileTitle.text = getString(R.string.delete_account)
                        with(leftButton) {
                            text = getString(R.string.cancel)
                            setOnClickListener {
                                dismiss()
                            }
                        }
                        with(rightButton) {
                            text = getString(R.string.delete)
                            setOnClickListener {

                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBottomSheet(view: View) {
        val modalBottomSheet =
            view.findViewById<ConstraintLayout>(R.id.profile_modal_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
        bottomSheetBehavior.skipCollapsed = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun startTransition(fragment: Fragment, tag: String) {
        childFragmentManager.commit {
            replace(
                R.id.profile_child_fragment_container,
                fragment,
                tag
            )
        }
    }

    companion object {
        private const val PROFILE_DELETE_TAG = "DeleteProfileFragment"
        private const val EDIT_PROFILE_TAG = "EditProfileFragment"
        private const val LOG_OUT_PROFILE_TAG = "LogOutFragment"
    }
}
