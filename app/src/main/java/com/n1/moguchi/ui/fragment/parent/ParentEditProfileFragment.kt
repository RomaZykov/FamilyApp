package com.n1.moguchi.ui.fragment.parent

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
    private var _binding: FragmentEditProfileParentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileParentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deleteProfileTv.setOnClickListener {
            parentFragmentManager.commit {
                remove(ParentEditProfileFragment())
                replace<DeleteParentFragment>(
                    R.id.primary_child_fragment_container, "DeleteParentFragment"
                )
                setReorderingAllowed(true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}