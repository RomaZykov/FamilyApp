package com.n1.moguchi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentFourSlidePageBinding

class FourSlideFragment : Fragment() {

    private lateinit var binding: FragmentFourSlidePageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFourSlidePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_onBoardingFragment_to_homeFragment)
        }
    }
}