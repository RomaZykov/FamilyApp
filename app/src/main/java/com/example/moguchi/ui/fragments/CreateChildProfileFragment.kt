package com.example.moguchi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moguchi.R

class CreateChildProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_child_profile, container, false)
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            CreateChildProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}