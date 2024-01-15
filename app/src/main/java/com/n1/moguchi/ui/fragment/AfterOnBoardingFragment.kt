package com.n1.moguchi.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.MaterialToolbar
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentAfterOnboardingBinding
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.fragment.parent.children_creation.ChildCreationFragment
import com.n1.moguchi.ui.fragment.parent.goal_creation.GoalCreationFragment
import com.n1.moguchi.ui.fragment.parent.password.PasswordFragment
import com.n1.moguchi.ui.fragment.parent.task_creation.TaskCreationFragment

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

        val navHostFragment = (activity as MainActivity)
            .supportFragmentManager
            .findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val fragments = listOf(
            ChildCreationFragment(),
            GoalCreationFragment(addChildButtonEnable = false, childSelectionEnable = false),
            TaskCreationFragment(),
            PasswordFragment()
        )
        val topAppBar = requireActivity().findViewById<MaterialToolbar>(R.id.top_common_app_bar)
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
                        moveToFragment(currentFragmentInContainer, fragments[1])
                    }

                    fragments[1] -> {
                        checkButtonPressed()
                        moveToFragment(currentFragmentInContainer, fragments[2])
                    }

                    fragments[2] -> {
                        checkButtonPressed()
                        moveToFragment(currentFragmentInContainer, fragments[3])
                    }

                    fragments[3] -> {
                        checkButtonPressed()
                        moveToFragmentByChildCreationCondition(
                            currentFragmentInContainer,
                            fragments[1],
                            navController,
                            isAllChildrenCompleted(fragments[1])
                        )
                    }
                }
                isButtonEnabled = false
                changeButton(isButtonEnabled)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isAllChildrenCompleted(fragment: Fragment): Boolean {
        val bundle = fragment.requireArguments()
        return bundle.getBoolean(GoalCreationFragment.GOAL_SETTING_FOR_CHILDREN_COMPLETED_KEY)
    }

    private fun moveToFragmentByChildCreationCondition(
        currentFragmentInContainer: Fragment,
        fragmentToMove: Fragment,
        navController: NavController,
        allChildrenCompleted: Boolean
    ) {
        if (allChildrenCompleted) {
            navController.navigate(R.id.action_afterOnBoardingFragment_to_parentHomeFragment)
        } else {
            moveToFragment(currentFragmentInContainer, fragmentToMove)
        }
    }

    private fun moveToFragment(
        currentFragmentInContainer: Fragment,
        fragment: Fragment
    ) {
        childFragmentManager.beginTransaction()
            .remove(currentFragmentInContainer)
            .replace(
                R.id.after_onboarding_fragment_container_view,
                fragment
            )
            .addToBackStack(null)
            .commit()
    }

    private fun checkButtonPressed() {
        childFragmentManager.setFragmentResult(
            "nextButtonPressed",
            bundleOf("buttonIsPressedKey" to true)
        )
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
}