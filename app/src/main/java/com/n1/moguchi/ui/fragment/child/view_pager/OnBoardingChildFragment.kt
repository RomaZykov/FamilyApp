package com.n1.moguchi.ui.fragment.child.view_pager

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
        binding.dotsLl.apply {
            for (i in 0 until NUM_PAGES) {
                val imageView = ImageView(requireContext())
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(8, 0, 8, 0)
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

        val bundle = childIdBundle()
        binding.skipButton.setOnClickListener {
            (activity as MainActivity).navController.navigate(
                R.id.action_onBoardingChildFragment_to_homeChildFragment,
                bundle
            )
        }

        binding.nextButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem != 1) {
                viewPager.setCurrentItem(currentItem + 1, true)
            } else {
                (activity as MainActivity).navController.navigate(
                    R.id.action_onBoardingChildFragment_to_homeChildFragment,
                    bundle
                )
            }
        }
    }

    private fun childIdBundle(): Bundle {
        val bundle = Bundle().apply {
            val childId = arguments?.getString("childId")
            this.putString("childId", childId)
        }
        return bundle
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
