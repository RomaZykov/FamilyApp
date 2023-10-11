package com.n1.moguchi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.AppBarLayout
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentAfterOnboardingBinding
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.fragments.parent.AddChildFragment
import com.n1.moguchi.ui.fragments.parent.GoalCreationFragment
import com.n1.moguchi.ui.fragments.parent.TaskCreationFragment

class AfterOnBoardingFragment : Fragment() {

    private var _binding: FragmentAfterOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAfterOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = requireActivity().findViewById<AppBarLayout>(R.id.commonAppBar)
//        topAppBar.setNavigationOnClickListener {
//            parentFragmentManager.commit {
//                remove(this@AfterOnBoardingFragment)
//            }
//        }

        val fragments = listOf(
            AddChildFragment(),
            GoalCreationFragment(),
            TaskCreationFragment(),
            PasswordFragment()
        )
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.after_onboarding_fragment_container_view, fragments[0])
            .commit()

        binding.cancelButton.setOnClickListener {

        }

        binding.nextButton.setOnClickListener {
            when (val currentFragmentInContainer =
                requireActivity().supportFragmentManager.fragments.last()) {
                fragments[0] -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .remove(currentFragmentInContainer)
                        .replace(R.id.after_onboarding_fragment_container_view, fragments[1])
                        .commit()
                }

                fragments[1] -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .remove(currentFragmentInContainer)
                        .replace(R.id.after_onboarding_fragment_container_view, fragments[2])
                        .commit()
                }

                fragments[2] -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .remove(currentFragmentInContainer)
                        .replace(R.id.after_onboarding_fragment_container_view, fragments[3])
                        .commit()
                }

                fragments[3] -> {
                    val navHostFragment = (activity as MainActivity)
                            .supportFragmentManager
                            .findFragmentById(R.id.fragment_container_view) as NavHostFragment
                    val navController = navHostFragment.navController
                    navController.navigate(R.id.action_afterOnBoardingFragment_to_parentHomeFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}