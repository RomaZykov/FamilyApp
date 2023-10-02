package com.n1.moguchi.ui.fragments.parent

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
    ): View? {
        binding = FragmentThirdSlidePageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        private const val AFTER_ONBOARDING = "after_onboarding"

        fun newInstance(): Fragment {
            return ThirdSlideFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(AFTER_ONBOARDING, true)
                }
            }
        }
    }
}