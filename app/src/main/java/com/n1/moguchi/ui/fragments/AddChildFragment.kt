package com.n1.moguchi.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentAddChildBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.adapters.ChildrenListAdapter
import com.n1.moguchi.ui.viewmodels.AddChildViewModel
import javax.inject.Inject

class AddChildFragment : Fragment() {

    private lateinit var binding: FragmentAddChildBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var childrenListAdapter: ChildrenListAdapter

    private var childrenCardList: MutableList<View> = mutableListOf()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AddChildViewModel::class.java]
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
        binding = FragmentAddChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        auth = Firebase.auth

        val recyclerView: RecyclerView = binding.rvChildrenList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        childrenListAdapter = ChildrenListAdapter(childrenCardList)
        recyclerView.adapter = childrenListAdapter

        binding.addChildButton.setOnClickListener {
            val childCard =
                layoutInflater.inflate(R.layout.child_creation_card, recyclerView, false)
            childrenCardList.add(childCard)
            childrenListAdapter.notifyItemInserted(0)
        }

        binding.saveChildrenButton.setOnClickListener {
            saveChildrenToFirebase(navController)
        }
    }

    private fun saveChildrenToFirebase(navController: NavController) {
        val parentId = auth.currentUser?.uid

        val args = arguments
        val isAfterOnBoarding = args?.getBoolean("onboarding_completed")

        if (parentId != null) {
            val childrenNamesList = childrenListAdapter.retrieveChildrenNames()
            viewModel.saveChildrenList(parentId, childrenNamesList)
            if (isAfterOnBoarding == true) {
                navController.navigate(R.id.action_addChildFragment_to_goalCreationFragment)
            } else {
                navController.navigate(R.id.homeFragment)
            }
        }
    }
}