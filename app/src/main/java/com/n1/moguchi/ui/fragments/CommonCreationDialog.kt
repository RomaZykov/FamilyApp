package com.n1.moguchi.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.databinding.DialogCommonCreationBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.viewmodels.ParentViewModel
import javax.inject.Inject

class CommonCreationDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogCommonCreationBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: ParentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ParentViewModel::class.java]
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
    ): View? {
        binding = DialogCommonCreationBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modalBottomSheet = view.findViewById<ConstraintLayout>(R.id.modal_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        val parentId = Firebase.auth.currentUser?.uid

        val childrenSpinner: Spinner = view.findViewById(R.id.children_spinner)
        if (parentId != null) {
            val childrenNames = mutableListOf<String>()
            viewModel.getChildrenList(parentId)
            viewModel.children.observe(viewLifecycleOwner) { children ->
                children?.forEach { childrenNames.add(it.childName!!) }
                ArrayAdapter(
                    requireActivity(),
                    android.R.layout.simple_list_item_1,
                    childrenNames
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    childrenSpinner.adapter = adapter
                }
            }
        } else {
            throw Exception("User not authorized")
        }

        binding.increaseButton.setOnClickListener {
            viewModel.increaseHeight()
        }
        binding.decreaseButton.setOnClickListener {
            viewModel.decreaseHeight()
        }

        binding.nextButton.setOnClickListener {
            val goalTitle = binding.goalNameEditText.editText?.text.toString().trim { it <= ' ' }
            if (goalTitle.isNotEmpty() && goalTitle.isNotBlank()) {
                val fragmentManager = requireActivity().supportFragmentManager
                val modalTaskCreationBottomSheet = TaskCreationDialog()
                modalTaskCreationBottomSheet.show(fragmentManager, TaskCreationDialog.TAG)
            }
        }
//
//        binding.cancelButton.setOnClickListener {
//
//        }
    }

    companion object {
        const val TAG = "CommonBottomSheet"
    }
}