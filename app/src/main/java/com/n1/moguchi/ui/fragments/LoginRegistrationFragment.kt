package com.n1.moguchi.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentLoginRegistrationBinding

class LoginRegistrationFragment : Fragment() {

    private lateinit var binding: FragmentLoginRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registrationButton.apply {
            setOnClickListener {
                binding.buttonNext.apply {
                    isEnabled = true
                    alpha = 1F
                    setOnClickListener {
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_loginRegistrationFragment_to_registrationFragment)
                    }
                }
            }
        }

        binding.loginButton.apply {
            setOnClickListener {
                binding.buttonNext.apply {
                    isEnabled = true
                    alpha = 1F
                    setOnClickListener {
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_loginRegistrationFragment_to_loginFragment)
                    }
                }
            }
        }
    }
}