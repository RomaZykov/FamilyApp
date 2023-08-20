package com.n1.moguchi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentParentProfileBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.viewmodels.HomeViewModel
import javax.inject.Inject

class ParentProfileFragment : Fragment() {

    private lateinit var binding: FragmentParentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var signInClient: SignInClient

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentProfileBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        signInClient = Identity.getSignInClient(requireContext())
        auth = Firebase.auth

        binding.signOutButton.setOnClickListener {
            signOut()
            signInClient.signOut().addOnCompleteListener(requireActivity()) {
                navController.navigate(R.id.action_parentProfileFragment_to_registrationFragment)
            }
        }

        binding.profileCard.editProfileButton.setOnClickListener {
            showParentEditDialog()
        }
    }

    private fun showParentEditDialog() {
        val fragmentManager = requireParentFragment().childFragmentManager
        val modalBottomSheet = ParentEditProfileBottomSheetFragment()
        modalBottomSheet.show(fragmentManager, ParentEditProfileBottomSheetFragment.TAG)
    }

    private fun signOut() {
        auth.signOut()
    }
}