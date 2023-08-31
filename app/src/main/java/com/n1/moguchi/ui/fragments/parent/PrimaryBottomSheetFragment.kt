package com.n1.moguchi.ui.fragments.parent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.databinding.FragmentPrimaryBottomSheetBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.viewmodels.PrimaryBottomSheetViewModel
import javax.inject.Inject

class PrimaryBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPrimaryBottomSheetBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PrimaryBottomSheetViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as MoguchiBaseApplication).appComponent
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrimaryBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modalBottomSheet = view.findViewById<ConstraintLayout>(R.id.modal_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
        bottomSheetBehavior.skipCollapsed = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        childFragmentManager.beginTransaction()
            .replace(R.id.child_fragment_container, GoalCreationFragment(), GOAL_CREATION_TAG)
            .commit()

        binding.nextButton.setOnClickListener {
            val currentGoalHeight = viewModel.goalHeight.value
            val goal = Goal(

            )
//            viewModel.createGoal(goal = goal)

            val currentFragmentInContainer = childFragmentManager.fragments[0]

            when (currentFragmentInContainer.tag) {
                GOAL_CREATION_TAG -> {
                    childFragmentManager.beginTransaction()
                        .remove(currentFragmentInContainer)
                        .replace(
                            R.id.child_fragment_container,
                            TaskCreationFragment(),
                            TASK_CREATION_TAG
                        )
                        .commit()
                }

                TASK_CREATION_TAG -> {
                    childFragmentManager.beginTransaction()
                        .remove(currentFragmentInContainer)
                        .replace(
                            R.id.child_fragment_container,
                            TaskCreationFragment(),
                            TASK_CREATION_TAG
                        )
                        .commit()
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        const val TAG = "PrimaryModalBottomSheet"
        const val GOAL_CREATION_TAG = "GoalCreationFragment"
        const val TASK_CREATION_TAG = "TaskCreationFragment"
    }
}