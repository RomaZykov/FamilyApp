package com.n1.moguchi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.MaterialToolbar
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentAfterOnboardingBinding
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.fragments.parent.children_creation.AddChildFragment
import com.n1.moguchi.ui.fragments.parent.goal_creation.GoalCreationFragment
import com.n1.moguchi.ui.fragments.parent.task_creation.TaskCreationFragment

class AfterOnBoardingFragment : Fragment() {

    private var _binding: FragmentAfterOnboardingBinding? = null
    private val binding get() = _binding!!
    private var isButtonEnabled: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAfterOnboardingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
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

        childFragmentManager.setFragmentResultListener(
            "buttonIsEnabled",
            viewLifecycleOwner
        ) { _, bundle ->
            val currentFragmentInContainer = childFragmentManager.fragments.last()
            isButtonEnabled = bundle.getBoolean("buttonIsReadyKey")
            changeButton(isButtonEnabled)
            binding.goNextButton.setOnClickListener {
                when (currentFragmentInContainer) {
                    fragments[0] -> {
                        childFragmentManager.beginTransaction()
                            .remove(currentFragmentInContainer)
                            .replace(
                                R.id.after_onboarding_fragment_container_view,
                                fragments[1]
                            )
                            .addToBackStack(null)
                            .commit()
                    }

                    fragments[1] -> {
                        childFragmentManager.beginTransaction()
                            .remove(currentFragmentInContainer)
                            .replace(
                                R.id.after_onboarding_fragment_container_view,
                                fragments[2]
                            )
                            .addToBackStack(null)
                            .commit()
                        childFragmentManager.setFragmentResult(
                            "nextButtonPressed",
                            bundleOf("buttonIsPressedKey" to true)
                        )
                    }

                    fragments[2] -> {
                        childFragmentManager.beginTransaction()
                            .remove(currentFragmentInContainer)
                            .replace(
                                R.id.after_onboarding_fragment_container_view,
                                fragments[3]
                            )
                            .addToBackStack(null)
                            .commit()
                    }

                    fragments[3] -> {
                        childFragmentManager.setFragmentResult(
                            "nextButtonPressed",
                            bundleOf("buttonIsPressedKey" to true)
                        )
                        val navHostFragment = (activity as MainActivity)
                            .supportFragmentManager
                            .findFragmentById(R.id.fragment_container_view) as NavHostFragment
                        val navController = navHostFragment.navController
                        if (GoalCreationFragment.selectedChildIndex < GoalCreationFragment.childrenSize) {
                            // If: return to GoalCreationFragment and continue set goal/task/password
                            childFragmentManager.beginTransaction()
                                .remove(currentFragmentInContainer)
                                .replace(
                                    R.id.after_onboarding_fragment_container_view,
                                    fragments[1]
                                )
                                .addToBackStack(null)
                                .commit()
                        } else {
                            // Else: parent set goal/task/password for all children
                            navController.navigate(R.id.action_afterOnBoardingFragment_to_parentHomeFragment)
                        }
                    }
                }
                isButtonEnabled = false
                changeButton(isButtonEnabled)
            }
        }
    }

    private fun changeButton(isButtonEnabled: Boolean?) {
        when (isButtonEnabled) {
            true -> {
                with(binding.goNextButton) {
                    isEnabled = true
                    setTextColor(context?.getColorStateList(R.color.white))
                    backgroundTintList = context?.getColorStateList(R.color.orange)
                }
            }

            false -> {
                with(binding.goNextButton) {
                    isEnabled = false
                    setTextColor(context?.getColorStateList(R.color.white_opacity_70))
                    backgroundTintList = context?.getColorStateList(R.color.orange_opacity_70)
                }
            }

            else -> throw NullPointerException("isButtonEnabled equals null")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}