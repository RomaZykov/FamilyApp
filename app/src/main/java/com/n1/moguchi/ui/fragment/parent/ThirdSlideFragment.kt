package com.n1.moguchi.ui.fragment.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.n1.moguchi.databinding.FragmentThirdSlidePageBinding

class ThirdSlideFragment : Fragment() {

    private lateinit var binding: FragmentThirdSlidePageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThirdSlidePageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}