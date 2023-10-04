package com.n1.moguchi.ui.fragments.child

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentOnboardingBinding
import com.n1.moguchi.ui.activity.MainActivity

private const val NUM_PAGES = 2

class OnBoardingChildFragment : Fragment() {

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

        viewPager = binding.onboardingPager
        val adapter = OnBoardingChildViewPagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle
        )
        viewPager.adapter = adapter

        binding.skipButton.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_onBoardingChildFragment_to_homeChildFragment)
        }

        binding.nextButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem != 1) {
                viewPager.setCurrentItem(currentItem + 1, true)
            } else {
                Log.d(
                    "OnBoardingChildFragment",
                    "Current destination = ${(activity as MainActivity).navController.currentDestination}"
                )
                (activity as MainActivity).navController.navigate(R.id.action_onBoardingChildFragment_to_homeChildFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class OnBoardingChildViewPagerAdapter(
        fm: FragmentManager,
        lifecycle: Lifecycle
    ) : FragmentStateAdapter(fm, lifecycle) {

        val onBoardingChildList = listOf(
            FirstSlideChildFragment(),
            SecondSlideChildFragment()
        )

        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            return onBoardingChildList[position]
        }
    }
}
