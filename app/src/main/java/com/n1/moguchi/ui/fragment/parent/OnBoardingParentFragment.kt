package com.n1.moguchi.ui.fragment.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentOnboardingBinding
import com.n1.moguchi.ui.activity.MainActivity

private const val NUM_PAGES = 3

class OnBoardingParentFragment : Fragment() {

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

        val bundle = Bundle()
        bundle.putBoolean("isFromOnBoarding", true)

        viewPager = binding.onboardingPager
        val adapter = OnBoardingViewPagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle
        )
        viewPager.adapter = adapter
        binding.dotsLl.apply {
            for (i in 0 until NUM_PAGES) {
                val imageView = ImageView(requireContext())
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(12, 0, 12, 0)
                imageView.layoutParams = params
                imageView.setBackgroundResource(R.drawable.default_dot)
                this.addView(imageView, i)
            }
        }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                for (i in 0 until NUM_PAGES) {
                    if (i == position) {
                        binding.dotsLl[i].setBackgroundResource(R.drawable.selected_dot)
                    } else {
                        binding.dotsLl[i].setBackgroundResource(R.drawable.default_dot)
                    }
                }
            }
        })

        binding.skipButton.setOnClickListener {
            (activity as MainActivity).navController.navigate(
                R.id.action_onBoardingParentFragment_to_afterOnBoardingFragment,
                bundle
            )
        }

        binding.nextButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem != 2) {
                viewPager.setCurrentItem(currentItem + 1, true)
            } else {
                (activity as MainActivity).navController.navigate(
                    R.id.action_onBoardingParentFragment_to_afterOnBoardingFragment,
                    bundle
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class OnBoardingViewPagerAdapter(
        fm: FragmentManager,
        lifecycle: Lifecycle
    ) : FragmentStateAdapter(fm, lifecycle) {

        val onBoardingList = listOf(
            FirstSlideFragment(),
            SecondSlideFragment(),
            ThirdSlideFragment()
        )

        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            return onBoardingList[position]
        }
    }
}
