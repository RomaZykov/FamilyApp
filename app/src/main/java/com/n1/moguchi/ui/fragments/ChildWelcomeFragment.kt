package com.n1.moguchi.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentChildWelcomeBinding

class ChildWelcomeFragment : Fragment() {

    private lateinit var binding: FragmentChildWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChildWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonParentConnection.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_childWelcomeFragment_to_loginRegistrationFragment)
        }
    }
}