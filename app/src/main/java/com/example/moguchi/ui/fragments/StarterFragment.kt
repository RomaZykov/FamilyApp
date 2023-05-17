package com.example.moguchi.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.moguchi.R
import com.example.moguchi.databinding.FragmentStarterBinding

class StarterFragment : Fragment() {

    private lateinit var binding: FragmentStarterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStarterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonChildAccount.apply {
            setOnClickListener {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_starterFragment_to_addChildNameFragment)
            }
        }

        binding.buttonParentAccount.apply {
            setOnClickListener {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_starterFragment_to_loginRegistrationFragment)
            }
        }
    }
}