package com.n1.moguchi.ui.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.databinding.DialogTaskCreationBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.viewmodels.ParentViewModel
import javax.inject.Inject

class TaskCreationDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogTaskCreationBinding

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
    ): View {
        binding = DialogTaskCreationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parentId = Firebase.auth.currentUser?.uid


    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR)
        return dialog
    }
}