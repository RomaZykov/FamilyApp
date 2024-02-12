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
import com.n1.moguchi.ui.fragment.parent.home.HomeParentFragment
import com.n1.moguchi.ui.fragment.parent.profile.related_bottom_sheet.ProfileContainerBottomSheetFragment
import javax.inject.Inject

class ProfileParentFragment : Fragment() {

    private var _binding: FragmentParentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ProfileParentViewModel::class.java]
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

        val parentId = requireArguments().getString(HomeParentFragment.PARENT_ID_KEY)

        val navHostFragment =
            requireParentFragment().parentFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val topAppBar = requireActivity().findViewById<Toolbar>(R.id.parent_profile_app_bar)
        topAppBar.setNavigationOnClickListener {
            navController.navigate(R.id.action_parentProfileFragment_to_parentHomeFragment)
        }

        if (parentId != null) {
//            lifecycleScope.launch {
//                repeatOnLifecycle(Lifecycle.State.STARTED) {
//                    viewModel.fetchParentData(parentId).collect {
//
//                    }
//                }
//            }
        }

        binding.signOutButton.setOnClickListener {
            showBottomSheet(LOG_OUT_TAG)
        }

        binding.profileCard.editProfileButton.setOnClickListener {
            showBottomSheet(EDIT_PROFILE_TAG)
        }

        binding.myChildrenButton.setOnClickListener {
            val bundle = bundleOf("isFromParentProfile" to true, "deleteChildOptionEnable" to true)
            navController.navigate(R.id.action_parentProfileFragment_to_addChildFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showBottomSheet(tag: String) {
        val fragmentManager = childFragmentManager
        fragmentManager.setFragmentResult(
            "profileBottomSheetRequestKey",
            bundleOf("profileBundleKey" to tag)
        )
        val modalBottomSheet = ProfileContainerBottomSheetFragment() as BottomSheetDialogFragment
        modalBottomSheet.show(fragmentManager, null)
    }

    companion object {
        private const val EDIT_PROFILE_TAG = "EditProfileIntent"
        private const val LOG_OUT_TAG = "LogOutIntent"
    }
}