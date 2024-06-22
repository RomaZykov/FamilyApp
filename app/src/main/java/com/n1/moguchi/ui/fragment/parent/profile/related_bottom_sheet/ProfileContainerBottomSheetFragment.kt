package com.n1.moguchi.ui.fragment.parent.profile.related_bottom_sheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.ProfileMode
import com.n1.moguchi.databinding.FragmentProfileBottomSheetBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.fragment.parent.profile.ProfileParentFragmentDirections
import com.n1.moguchi.ui.fragment.parent.profile.related_bottom_sheet.edit_profile.EditParentProfileFragment
import javax.inject.Inject

class ProfileContainerBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentProfileBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var signInClient: SignInClient
    private lateinit var auth: FirebaseAuth

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ProfileBottomSheetViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as MoguchiBaseApplication).appComponent
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

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

        val parentId = requireArguments().getString(PARENT_ID_KEY)!!
        val photoUrl = Firebase.auth.currentUser?.photoUrl.toString()

        setFragmentResultListener(
            "profileBottomSheetRequestKey"
        ) { _, bundle ->
            when (bundle.getString("profileBundleKey")) {
                "EditProfileIntent" -> {
                    binding.editProfileTitle.text = getString(R.string.edit)
                    startTransition(
                        EditParentProfileFragment.newInstance(parentId, photoUrl),
                        EDIT_PROFILE_TAG
                    )
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
                                viewModel.updateUserPrefs(ProfileMode.UNDEFINED)
                                signInClient.signOut().addOnCompleteListener {
                                    dismiss()
                                    val action =
                                        ProfileParentFragmentDirections.actionParentProfileFragmentToRegistrationFragment()
                                    navController.navigate(action)
                                }
                            }
                        }
                    }

                    PROFILE_DELETE_TAG -> {
                        binding.editProfileTitle.text = getString(R.string.delete_account)
                        with(leftButton) {
                            text = getString(R.string.cancel)
                            setTextColor(context?.getColorStateList(R.color.white))
                            backgroundTintList = context?.getColorStateList(R.color.orange)
                            setOnClickListener {
                                dismiss()
                            }
                        }
                        with(rightButton) {
                            text = getString(R.string.delete)
                            setTextColor(context?.getColorStateList(R.color.black))
                            backgroundTintList = context?.getColorStateList(R.color.white)
                            setOnClickListener {
                                viewModel.deleteAccountWithAllRelatedData(parentId)
                                dismiss()
                                val action =
                                    ProfileParentFragmentDirections.actionParentProfileFragmentToRegistrationFragment()
                                navController.navigate(action)
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

        private const val PARENT_ID_KEY = "parentIdKey"
        private const val PARENT_PROFILE_URL_KEY = "parentProfileUrlKey"

        fun newInstance(parentId: String): ProfileContainerBottomSheetFragment {
            return ProfileContainerBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(PARENT_ID_KEY, parentId)
                    putString(PARENT_PROFILE_URL_KEY, parentId)
                }
            }
        }
    }
}
