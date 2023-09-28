package com.n1.moguchi.ui.fragments.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentChildOnboardingBinding

class OnBoardingChildFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var binding: FragmentChildOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChildOnboardingBinding.inflate(layoutInflater, container, false)

        val fragmentList = arrayListOf(
            FirstSlideChildFragment(),
            SecondSlideChildFragment()
        )
        viewPager = binding.childOnboardingPager
//        val adapter = OnBoardingViewPagerAdapter(
//            fragmentList,
//            requireActivity().supportFragmentManager,
//            lifecycle
//        )
//        viewPager.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.skipButton.setOnClickListener {
            navController.navigate(R.id.action_onBoardingFragment_to_addChildFragment)
        }
    }
}
