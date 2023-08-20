package com.n1.moguchi.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentGoalCreationBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.viewmodels.PrimaryBottomSheetViewModel
import javax.inject.Inject

class GoalCreationFragment : Fragment() {

    private lateinit var binding: FragmentGoalCreationBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: PrimaryBottomSheetViewModel by lazy {
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoalCreationBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parentId = Firebase.auth.currentUser?.uid

        val childrenNames = mutableListOf<String>()
        if (parentId != null) {
            viewModel.getChildren(parentId)
            viewModel.children.observe(viewLifecycleOwner) { children ->
                children?.forEach { childrenNames.add(it.childName!!) }
                val childrenLinearLayout =
                    view.findViewById<LinearLayout>(R.id.children_list_ll)
                childrenNames.mapIndexed { index, name ->
                    val childItem =
                        layoutInflater.inflate(R.layout.small_child_item, childrenLinearLayout, false)
                    childItem.findViewById<TextView>(R.id.child_name).text = name
                    childrenLinearLayout.addView(childItem, index)
                }
            }
        } else {
            throw Exception("User not authorized")
        }

        viewModel.goalHeight.observe(viewLifecycleOwner) {
            binding.goalIncreaseButton.setOnClickListener {
                viewModel.increaseHeight()
            }
        }

        viewModel.goalHeight.observe(viewLifecycleOwner) {
            binding.goalDecreaseButton.setOnClickListener {
                viewModel.decreaseHeight()
            }
        }

//        binding.nextButton.setOnClickListener {
//            val currentGoalHeight = viewModel.goalHeight.value
//            val goal = Goal(
//
//            )
////            viewModel.createGoal(goal = goal)
//            parentFragmentManager.beginTransaction()
//                .remove(GoalCreationFragment())
//                .replace(R.id.child_fragment_container, TaskCreationFragment())
//                .commit()
//        }
    }
}