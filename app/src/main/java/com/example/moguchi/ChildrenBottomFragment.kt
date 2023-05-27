package com.example.moguchi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moguchi.databinding.FragmentChildrenBottomBinding
import com.example.moguchi.databinding.FragmentHomeBottomBinding

class ChildrenBottomFragment : Fragment() {

    private lateinit var binding: FragmentChildrenBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChildrenBottomBinding.inflate(inflater, container, false)
        return binding.root
    }
}