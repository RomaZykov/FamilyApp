package com.n1.moguchi.ui.fragments.parent

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentGoalCreationBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.viewmodels.GoalCreationViewModel
import javax.inject.Inject

class GoalCreationFragment : Fragment() {

    private var _binding: FragmentGoalCreationBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: GoalCreationViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GoalCreationViewModel::class.java]
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
        _binding = FragmentGoalCreationBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parentId = Firebase.auth.currentUser?.uid

        val childrenRadioGroup = view.findViewById<RadioGroup>(R.id.children_radio_group)
        if (parentId != null) {
            viewModel.getChildren(parentId)
            viewModel.children.observe(viewLifecycleOwner) { children ->
                children?.forEach { child ->
                    val childItem =
                        layoutInflater.inflate(
                            R.layout.small_child_item,
                            childrenRadioGroup,
                            false
                        )
                    childItem.findViewById<TextView>(R.id.small_child_name).text = child.childName
                    childItem.findViewById<ImageView>(R.id.small_child_avatar)
                        .setImageResource(child.imageResourceId!!)
                    childrenRadioGroup.addView(childItem)
                    (childrenRadioGroup.getChildAt(0) as MaterialCardView).isChecked = true
                }
            }
        } else {
            throw Exception("User not authorized")
        }

        childrenRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            (radioGroup.getChildAt(1) as MaterialCardView).isChecked = true
        }

        binding.goalName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                if (text.toString().isNotEmpty()) {
                    parentFragmentManager.setFragmentResult(
                        "buttonIsEnabled",
                        bundleOf("buttonIsReadyKey" to true)
                    )
                } else {
                    binding.goalName.error = "Добавьте цель"
                    parentFragmentManager.setFragmentResult(
                        "buttonIsEnabled",
                        bundleOf("buttonIsReadyKey" to false)
                    )
                }
            }
        })

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}