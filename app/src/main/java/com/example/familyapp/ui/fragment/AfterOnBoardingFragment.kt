package com.example.familyapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.familyapp.FamilyAppBaseApplication
import com.example.familyapp.R
import com.example.familyapp.data.remote.model.Child
import com.example.familyapp.data.remote.model.Goal
import com.example.familyapp.data.ProfileMode
import com.example.familyapp.data.remote.model.Task
import com.example.familyapp.databinding.FragmentAfterOnboardingBinding
import com.example.familyapp.ui.ViewModelFactory
import com.example.familyapp.ui.fragment.parent.child_creation.ChildCreationFragment
import com.example.familyapp.ui.fragment.parent.goal_creation.GoalCreationFragment
import com.example.familyapp.ui.fragment.parent.task_creation.TaskCreationFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class AfterOnBoardingFragment : Fragment() {

    private var _binding: FragmentAfterOnboardingBinding? = null
    private val binding get() = _binding!!

    private var isButtonEnabled: Boolean? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AfterOnBoardingViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as FamilyAppBaseApplication).appComponent
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

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

        val navController = findNavController()

        val fragments = listOf(
            ChildCreationFragment.newInstance(
                isFromOnBoarding = true, isFromParentProfile = false,
                isFromParentHome = false, deleteChildOptionEnable = true
            ),
            GoalCreationFragment.newInstance(
                addChildButtonEnable = false,
                childSelectionEnable = false,
                insideBottomSheetShouldOpen = false,
                isFromOnBoarding = true
            ),
            TaskCreationFragment(),
            PasswordFragment()
        )

        childFragmentManager.commit {
            replace(R.id.after_onboarding_fragment_container_view, fragments[0])
                .addToBackStack("ChildCreation")
        }

        childFragmentManager.setFragmentResultListener(
            "isButtonEnabledRequestKey",
            viewLifecycleOwner
        ) { _, bundle ->
            val currentFragmentInContainer = childFragmentManager.fragments.last()
            isButtonEnabled = bundle.getBoolean("buttonIsReadyKey")
            changeButton(isButtonEnabled)

            binding.goNextButton.setOnClickListener {
                when (currentFragmentInContainer) {
                    fragments[0] -> {
                        checkButtonPressed()
                        childFragmentManager.setFragmentResultListener(
                            CREATE_BUNDLE_REQUEST_KEY,
                            viewLifecycleOwner
                        ) { _, innerBundle ->
                            if (arguments != null) {
                                this.arguments?.putAll(innerBundle)
                            } else {
                                this.arguments = innerBundle
                            }
                        }
                        moveToFragment(currentFragmentInContainer, fragments[1])
                    }

                    fragments[1] -> {
                        checkButtonPressed()
                        childFragmentManager.setFragmentResultListener(
                            "goalCreationRequestKey",
                            viewLifecycleOwner
                        ) { _, innerBundle ->
                            if (arguments != null) {
                                this.arguments?.putAll(innerBundle)
                            } else {
                                this.arguments = innerBundle
                            }
                        }
                        moveToFragment(currentFragmentInContainer, fragments[2])
                    }

                    fragments[2] -> {
                        checkButtonPressed()
                        moveToFragment(currentFragmentInContainer, fragments[3])
                    }

                    fragments[3] -> {
                        checkButtonPressed()
                        childFragmentManager.setFragmentResultListener(
                            "childCreationProcessCompletedRequestKey",
                            viewLifecycleOwner
                        ) { _, innerBundle ->
                            if (arguments != null) {
                                this.arguments?.putAll(innerBundle)
                            } else {
                                this.arguments = innerBundle
                            }
                        }
                        moveToFragmentByChildCreationCondition(
                            currentFragmentInContainer,
                            fragments[1],
                            navController,
                            isAllChildrenCompleted()
                        )
                    }
                }
                changeButton(isButtonEnabled = false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isAllChildrenCompleted(): Boolean {
        val args = requireArguments()
        val children = args.getParcelableArrayList<Child>("children")
        if (children != null) {
            for (child in children) {
                args.getString(child.childId) ?: return false
            }
        }
        return true
    }

    // TODO - Rename function or reorganise it
    private fun moveToFragmentByChildCreationCondition(
        currentFragmentInContainer: Fragment,
        fragmentToMove: Fragment,
        navController: NavController,
        allChildrenCompleted: Boolean
    ) {
        if (allChildrenCompleted) {
            val childrenToParce = mutableListOf<Child>()
            requireArguments().getParcelableArrayList<Child>("children")?.forEach { child ->
                val password = requireArguments().getString(child.childId)
                if (password != null) {
                    childrenToParce.add(child.copy(passwordFromParent = password.toInt()))
                }
            }
            val tasks = requireArguments().getParcelableArrayList<Task>("tasks")
            val goals = requireArguments().getParcelableArrayList<Goal>("goals")?.toSet()
            if (goals != null && tasks != null) {
                viewModel.saveChildrenDataToDb(
                    childrenToParce.toList(),
                    goals.toList(),
                    tasks.toList()
                )
            }

            lifecycleScope.launch {
                viewModel.updateUserPrefs(ProfileMode.PARENT_MODE)
            }

            val action =
                AfterOnBoardingFragmentDirections.actionAfterOnBoardingFragmentToParentHomeFragment()
            navController.navigate(action)
        } else {
            moveToFragment(currentFragmentInContainer, fragmentToMove)
        }
    }

    private fun moveToFragment(
        currentFragmentInContainer: Fragment,
        fragment: Fragment
    ) {
        childFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .remove(currentFragmentInContainer)
            .replace(
                R.id.after_onboarding_fragment_container_view,
                fragment
            )
            .commit()
    }

    private fun checkButtonPressed() {
        childFragmentManager.setFragmentResult(
            NEXT_BUTTON_PRESSED_REQUEST_KEY,
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

    companion object {
        private const val CREATE_BUNDLE_REQUEST_KEY = "createBundleRequestKey"
        private const val NEXT_BUTTON_PRESSED_REQUEST_KEY = "nextButtonPressedRequestKey"
    }
}