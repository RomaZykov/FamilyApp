package com.example.moguchi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.moguchi.R
import com.example.moguchi.databinding.FragmentAddChildNameBinding

class AddChildNameFragment : Fragment() {

    private lateinit var binding: FragmentAddChildNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddChildNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNext.apply {
            isEnabled = true
            setOnClickListener {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_addChildNameFragment_to_childWelcomeFragment)
            }
        }
    }
}