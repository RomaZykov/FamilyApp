package com.n1.moguchi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentOnboardingBinding
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.adapters.OnBoardingViewPagerAdapter
import com.n1.moguchi.ui.fragments.parent.FirstSlideFragment
import com.n1.moguchi.ui.fragments.parent.SecondSlideFragment
import com.n1.moguchi.ui.fragments.parent.ThirdSlideFragment

class OnBoardingFragment(
    private val onBoardingPagesMap: Map<String, Fragment> = emptyMap(),
    private val pageLimit: Int = 3
) : Fragment() {

    private lateinit var viewPager: ViewPager2
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parentMap = mapOf(
            "first" to FirstSlideFragment(),
            "second" to SecondSlideFragment(),
            "third" to ThirdSlideFragment()
        )
        viewPager = binding.onboardingPager
        val adapter = OnBoardingViewPagerAdapter(
            onBoardingPagesMap.ifEmpty { parentMap },
            requireActivity().supportFragmentManager,
            lifecycle
        )
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = pageLimit

        binding.skipButton.setOnClickListener {
            (activity as MainActivity).navController.navigate(
                if (pageLimit == 3) {
                    R.id.action_onBoardingFragment_to_addChildFragment
                } else {
                    R.id.action_onBoardingFragment_to_homeChildFragment
                }
            )
        }

        binding.nextButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (pageLimit == 3) {
                if (currentItem != 2) {
                    viewPager.setCurrentItem(currentItem + 1, true)
                } else {
                    (activity as MainActivity).navController.navigate(R.id.action_onBoardingFragment_to_addChildFragment)
                }
            } else {
                if (currentItem != 1) {
                    viewPager.setCurrentItem(currentItem + 1, true)
                } else {
                    (activity as MainActivity).navController.navigate(R.id.action_onBoardingFragment_to_homeChildFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
