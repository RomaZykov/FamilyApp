package com.n1.moguchi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentDeleteParentBottomSheetBinding
import com.n1.moguchi.databinding.FragmentEditProfileParentBinding

class ParentEditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileParentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileParentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deleteProfileTv.setOnClickListener {
            val currentFragmentInContainer = parentFragmentManager.fragments[0]
            parentFragmentManager.beginTransaction()
                .remove(currentFragmentInContainer)
                .replace(
                    R.id.child_fragment_container, DeleteParentFragment(), DeleteParentFragment.TAG
                )
                .commit()
        }
    }

    class DeleteParentFragment : Fragment() {
        private lateinit var binding: FragmentDeleteParentBottomSheetBinding
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentDeleteParentBottomSheetBinding.inflate(inflater, container, false)
            return binding.root
        }

        companion object {
            const val TAG = "DeleteParentFragment"
        }
    }

}