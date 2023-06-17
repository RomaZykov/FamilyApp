package com.example.moguchi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.moguchi.R
import com.example.moguchi.ui.adapters.PagerAdapter

class OnBoardingFragment : Fragment() {

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding, container, false)

        val fragmentList = arrayListOf(
            FirstSlideFragment(),
            SecondSlideFragment(),
            ThirdSlideFragment(),
            FourSlideFragment()
        )

        viewPager = view.findViewById(R.id.pager)

        val adapter = PagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        viewPager.adapter = adapter

        return view
    }
}