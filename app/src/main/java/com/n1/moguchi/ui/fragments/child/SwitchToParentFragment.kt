package com.n1.moguchi.ui.fragments.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.n1.moguchi.databinding.FragmentSwitchToParentBinding

class SwitchToParentFragment : Fragment() {

    private lateinit var binding: FragmentSwitchToParentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentSwitchToParentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}