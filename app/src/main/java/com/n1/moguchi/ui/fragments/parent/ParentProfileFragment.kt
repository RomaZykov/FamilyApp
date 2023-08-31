package com.n1.moguchi.ui.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.databinding.FragmentParentProfileBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.viewmodels.HomeViewModel
import javax.inject.Inject

class ParentProfileFragment : Fragment() {

    private lateinit var binding: FragmentParentProfileBinding

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

        binding.signOutButton.setOnClickListener {
            showBottomSheet(ParentLogOutBottomSheetFragment())
        }

        binding.profileCard.editProfileButton.setOnClickListener {
            showBottomSheet(ParentEditProfileBottomSheetFragment())
        }
    }

    private fun showBottomSheet(fragment: Fragment) {
        val fragmentManager = requireParentFragment().childFragmentManager
        val modalBottomSheet = fragment as BottomSheetDialogFragment
        modalBottomSheet.show(fragmentManager, modalBottomSheet.tag)
    }
}