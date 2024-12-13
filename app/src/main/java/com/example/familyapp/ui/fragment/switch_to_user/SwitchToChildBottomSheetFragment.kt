package com.example.familyapp.ui.fragment.switch_to_user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.familyapp.FamilyAppBaseApplication
import com.example.familyapp.R
import com.example.familyapp.data.ProfileMode
import com.example.familyapp.databinding.FragmentSwitchToChildBottomSheetBinding
import com.example.familyapp.ui.ViewModelFactory
import com.example.familyapp.ui.activity.MainActivity
import com.example.familyapp.ui.adapter.ChildrenRecyclerAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class SwitchToChildBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSwitchToChildBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var childrenRecyclerAdapter: ChildrenRecyclerAdapter
    private var currentProfileMode: String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SwitchToUserViewModel::class.java]
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
        super.onCreate(savedInstanceState)
        _binding = FragmentSwitchToChildBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parentId = Firebase.auth.currentUser?.uid

        val navController = findNavController()

        showRelatedBottomSheet(view)

        lifecycleScope.launch {
            viewModel.getUserPrefs().collect {
                currentProfileMode = it.currentProfileMode
            }
        }

        if (parentId != null) {
            viewModel.getChildren(parentId)
            viewModel.children.observe(this) { children ->
                if (currentProfileMode == "parent_mode") {

                    val recyclerView: RecyclerView =
                        view.findViewById(R.id.rv_switch_to_child_children_list)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    childrenRecyclerAdapter = ChildrenRecyclerAdapter(children.toMutableList())
                    recyclerView.adapter = childrenRecyclerAdapter

                    childrenRecyclerAdapter.onChildClicked = { _, child ->
                        if (child != null) {
                            viewModel.updateUserPrefs(ProfileMode.CHILD_MODE, child.childId)
                        }

                        (requireActivity() as MainActivity).supportFragmentManager.setFragmentResult(
                            "changeProfileModeRequestKey",
                            bundleOf()
                        )
                        val bundle = Bundle()
                        if (child != null) {
                            bundle.putString("childId", child.childId)
                        }

                        navController.navigate(
                            R.id.action_parentHomeFragment_to_onBoardingChildFragment,
                            bundle
                        )
                        dismiss()
                    }
                }

            }
        }

        binding.title.setText(R.string.switch_to_child)
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun showRelatedBottomSheet(view: View) {
        val modalBottomSheet =
            view.findViewById<ConstraintLayout>(R.id.switch_to_child_bottom_sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(modalBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}