package com.example.moguchi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moguchi.databinding.FragmentHomeBottomBinding

class HomeBottomFragment : Fragment() {

    private lateinit var binding: FragmentHomeBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBottomBinding.inflate(inflater, container, false)
        return binding.root
    }


}