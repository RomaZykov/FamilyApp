package com.n1.moguchi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.n1.moguchi.databinding.FragmentSettingsBottomBinding

class SettingsBottomFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBottomBinding.inflate(inflater, container, false)
        return binding.root
    }
}