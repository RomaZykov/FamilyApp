package com.n1.moguchi.ui.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentParentLogoutBinding

class ParentLogOutBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentParentLogoutBinding
    private lateinit var signInClient: SignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentLogoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = requireParentFragment()
            .requireActivity()
            .supportFragmentManager
            .findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        signInClient = Identity.getSignInClient(requireContext())
        auth = Firebase.auth

        val modalBottomSheet =
            view.findViewById<ConstraintLayout>(R.id.parent_logout_modal_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
        bottomSheetBehavior.skipCollapsed = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.exitButton.setOnClickListener {
            signOut()
            signInClient.signOut().addOnCompleteListener {
                dismiss()
                navController.navigate(R.id.action_parentProfileFragment_to_registrationFragment)
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun signOut() {
        auth.signOut()
    }

    companion object {
        const val TAG = "ParentLogOutModalBottomSheet"
    }
}