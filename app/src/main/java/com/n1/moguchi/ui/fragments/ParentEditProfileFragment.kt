package com.n1.moguchi.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentEditProfileParentBinding
import com.n1.moguchi.databinding.FragmentGoalCreationBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.viewmodels.PrimaryBottomSheetViewModel
import javax.inject.Inject

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
            val fragment = childFragmentManager.findFragmentById(R.id.delete_profile)
            if (fragment != null) {
                parentFragmentManager.beginTransaction()
                    .remove(ParentEditProfileFragment())
                    .replace(
                        R.id.child_fragment_container,
                        fragment
                    )
                    .commit()
            }
        }
    }
}