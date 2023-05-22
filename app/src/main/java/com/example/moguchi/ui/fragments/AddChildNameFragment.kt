package com.example.moguchi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        val childNameEditText = binding.nameEditText.editText.toString()

        if (binding.nameEditText.editText != null) {
            binding.buttonNext.apply {
                isEnabled = true
                setOnClickListener {
                    val action =
                        AddChildNameFragmentDirections.actionAddChildNameFragmentToChildWelcomeFragment(
                            childNameEditText
                        )
                    findNavController().navigate(action)
                }
            }
        }
    }
}