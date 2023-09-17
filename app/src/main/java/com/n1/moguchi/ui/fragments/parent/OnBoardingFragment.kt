package com.n1.moguchi.ui.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentOnboardingBinding
import com.n1.moguchi.ui.adapters.OnBoardingViewPagerAdapter

class OnBoardingFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val fragmentMap = mapOf(
            "first" to FirstSlideFragment(),
            "second" to SecondSlideFragment(),
            "third" to ThirdSlideFragment()
        )
        viewPager = binding.onboardingPager
        val adapter = OnBoardingViewPagerAdapter(
            fragmentMap,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3

        binding.skipButton.setOnClickListener {
            navController.navigate(R.id.action_onBoardingFragment_to_addChildFragment)
        }

        binding.nextButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem != 2) {
                viewPager.setCurrentItem(currentItem + 1, true)
            } else {
                navController.navigate(R.id.action_onBoardingFragment_to_addChildFragment)
            }
        }
    }
}
