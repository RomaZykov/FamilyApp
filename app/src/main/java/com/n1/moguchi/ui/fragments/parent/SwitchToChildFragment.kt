package com.n1.moguchi.ui.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.n1.moguchi.databinding.FragmentSwitchToChildBinding

class SwitchToChildFragment : Fragment() {

    private lateinit var binding: FragmentSwitchToChildBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentSwitchToChildBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}