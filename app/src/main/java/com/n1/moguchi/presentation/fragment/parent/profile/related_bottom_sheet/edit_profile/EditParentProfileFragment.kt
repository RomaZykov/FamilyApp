package com.n1.moguchi.presentation.fragment.parent.profile.related_bottom_sheet.edit_profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.databinding.FragmentParentEditProfileBinding
import com.n1.moguchi.presentation.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditParentProfileFragment : Fragment() {

    private var _binding: FragmentParentEditProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[EditParentViewModel::class.java]
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
        _binding = FragmentParentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parentId = requireArguments().getString(PARENT_ID_KEY)!!
        val parentPhotoUrl = requireArguments().getString(PARENT_PROFILE_URL_KEY)!!

        binding.deleteProfileTv.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                "deleteAccountPressedRequestKey",
                bundleOf("buttonIsPressedKey" to true)
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchParentData(parentId).collect {
                    binding.parentNameEditText.setText(it.parentName)
                }
            }
        }
        viewModel.load(parentPhotoUrl, binding.profileImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val PARENT_ID_KEY = "parentIdKey"
        private const val PARENT_PROFILE_URL_KEY = "parentProfileUrlKey"

        fun newInstance(parentId: String, photoUrl: String): EditParentProfileFragment {
            return EditParentProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(PARENT_ID_KEY, parentId)
                    putString(PARENT_PROFILE_URL_KEY, photoUrl)
                }
            }
        }
    }
}