package com.n1.moguchi.ui.fragment.switch_to_user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.models.ProfileMode
import com.n1.moguchi.databinding.FragmentSwitchToChildBottomSheetBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.adapter.ChildrenRecyclerAdapter
import javax.inject.Inject

class SwitchToChildBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSwitchToChildBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var childrenRecyclerAdapter: ChildrenRecyclerAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SwitchToUserViewModel::class.java]
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
        super.onCreate(savedInstanceState)
        _binding = FragmentSwitchToChildBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parentId = Firebase.auth.currentUser?.uid

        val navController = findNavController()

        val currentProfileMode = viewModel.getProfileMode()
        showRelatedBottomSheet(view)

        if (parentId != null) {
            viewModel.getChildren(parentId)
            viewModel.children.observe(this) { children ->

                if (currentProfileMode == ProfileMode.PARENT_MODE) {

                    val recyclerView: RecyclerView =
                        view.findViewById(R.id.rv_switch_to_child_children_list)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    childrenRecyclerAdapter = ChildrenRecyclerAdapter(children.toMutableList())
                    recyclerView.adapter = childrenRecyclerAdapter

                    childrenRecyclerAdapter.onChildClicked = { _, childId ->
                        (requireActivity() as MainActivity).supportFragmentManager.setFragmentResult(
                            "changeProfileModeRequestKey",
                            bundleOf()
                        )
                        val bundle = Bundle()
                        bundle.putString("childId", childId)
                        navController.navigate(R.id.action_parentHomeFragment_to_onBoardingChildFragment, bundle)
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