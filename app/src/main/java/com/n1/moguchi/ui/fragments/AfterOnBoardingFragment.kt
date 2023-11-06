package com.n1.moguchi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.MaterialToolbar
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

        val topAppBar = requireActivity().findViewById<MaterialToolbar>(R.id.top_common_app_bar)
        val fragments = listOf(
            AddChildFragment(),
            GoalCreationFragment(),
            TaskCreationFragment(),
            PasswordFragment()
        )
        topAppBar.setNavigationOnClickListener {
            if (childFragmentManager.fragments.last() != fragments[0] && childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
            }
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.after_onboarding_fragment_container_view, fragments[0])
            .commit()

        binding.goNextButton.setOnClickListener {
            val currentFragmentInContainer =
                childFragmentManager.fragments.last()
            when (currentFragmentInContainer) {
                fragments[0] -> {
                    childFragmentManager.beginTransaction()
                        .remove(currentFragmentInContainer)
                        .replace(R.id.after_onboarding_fragment_container_view, fragments[1])
                        .addToBackStack(null)
                        .commit()
                }

                fragments[1] -> {
                    childFragmentManager.beginTransaction()
                        .remove(currentFragmentInContainer)
                        .replace(R.id.after_onboarding_fragment_container_view, fragments[2])
                        .addToBackStack(null)
                        .commit()
                }

                fragments[2] -> {
                    childFragmentManager.beginTransaction()
                        .remove(currentFragmentInContainer)
                        .replace(R.id.after_onboarding_fragment_container_view, fragments[3])
                        .addToBackStack(null)
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