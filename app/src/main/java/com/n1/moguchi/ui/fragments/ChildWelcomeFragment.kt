package com.n1.moguchi.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentChildWelcomeBinding

class ChildWelcomeFragment : Fragment() {

    private lateinit var binding: FragmentChildWelcomeBinding
    private val args: ChildWelcomeFragmentArgs by navArgs<ChildWelcomeFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChildWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val childName = args.welcomeFragmentArgs
        binding.tvDescription.text = String.format(getString(R.string.child_welcome), childName)
    }
}