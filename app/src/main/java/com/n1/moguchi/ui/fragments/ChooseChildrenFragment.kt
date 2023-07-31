package com.n1.moguchi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.n1.moguchi.databinding.FragmentChooseChildrenBinding

class ChooseChildrenFragment : Fragment() {

    private lateinit var binding: FragmentChooseChildrenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentChooseChildrenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}