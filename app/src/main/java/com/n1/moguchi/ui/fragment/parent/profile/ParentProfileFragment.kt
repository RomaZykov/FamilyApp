package com.n1.moguchi.ui.fragment.parent.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentParentProfileBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.fragment.parent.home.ParentHomeViewModel
import javax.inject.Inject

class ParentProfileFragment : Fragment() {

    private var _binding: FragmentParentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
//        ViewModelProvider(this, viewModelFactory)[ParentHomeViewModel::class.java] - Incorrect vm
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParentProfileBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            requireParentFragment().parentFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val topAppBar = requireActivity().findViewById<Toolbar>(R.id.parent_profile_app_bar)
        topAppBar.setNavigationOnClickListener {
            navController.navigate(R.id.action_parentProfileFragment_to_parentHomeFragment)
        }

        binding.signOutButton.setOnClickListener {
            showBottomSheet(ParentLogOutBottomSheetFragment())
        }

        binding.profileCard.editProfileButton.setOnClickListener {
            showBottomSheet(ParentEditProfileBottomSheetFragment())
        }

        binding.myChildrenButton.setOnClickListener {
            val bundle = bundleOf("isFromParentProfile" to true)
            navController.navigate(R.id.action_parentProfileFragment_to_addChildFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showBottomSheet(fragment: Fragment) {
        val fragmentManager = requireParentFragment().childFragmentManager
        val modalBottomSheet = fragment as BottomSheetDialogFragment
        modalBottomSheet.show(fragmentManager, modalBottomSheet.tag)
    }
}