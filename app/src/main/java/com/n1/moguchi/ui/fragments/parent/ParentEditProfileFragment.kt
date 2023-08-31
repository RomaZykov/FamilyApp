package com.n1.moguchi.ui.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.n1.moguchi.R
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
            parentFragmentManager.commit {
                remove(ParentEditProfileFragment())
                replace<DeleteParentFragment>(
                    R.id.child_fragment_container, "DeleteParentFragment"
                )
                setReorderingAllowed(true)
            }
        }
    }
}