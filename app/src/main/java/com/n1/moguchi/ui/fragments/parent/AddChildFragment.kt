package com.n1.moguchi.ui.fragments.parent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.repositories.ParentRepository
import com.n1.moguchi.databinding.FragmentAddChildBinding
import com.n1.moguchi.ui.ChildCardListener
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.adapters.ChildrenRecyclerAdapter
import com.n1.moguchi.ui.viewmodels.AddChildViewModel
import javax.inject.Inject

class AddChildFragment(private val parentRepository: ParentRepository) : Fragment() {

    private var _binding: FragmentAddChildBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var childrenRecyclerAdapter: ChildrenRecyclerAdapter
    private lateinit var childCardListener: ChildCardListener

    private var childrenCardList: MutableList<Any> = mutableListOf()

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
        _binding = FragmentAddChildBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val isFromParentHome = arguments?.getBoolean("isFromParentHome")
        val isFromParentProfile = arguments?.getBoolean("isFromParentProfile")

        val recyclerView: RecyclerView = binding.rvChildrenList
        val childCard =
            layoutInflater.inflate(R.layout.child_creation_card, recyclerView, false)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        childrenCardList.add(childCard)
        childrenRecyclerAdapter = ChildrenRecyclerAdapter(childrenCardList)
        recyclerView.adapter = childrenRecyclerAdapter

        val children = object : ChildCardListener {
            override fun retrieveChildrenNamesFromCard(childrenByCardPosition: MutableMap<Int, String>) {
                viewModel.childrenNames.observe(viewLifecycleOwner) {
                    TODO()
                }
            }
        }
//        if ( && ) {
//
//            binding.addChildButton.background.alpha = 192
//            binding.addChildButton.isEnabled = false
//            binding.saveChildrenButton.isEnabled = false
//            binding.saveChildrenButton.background.alpha = 192
//        }
//        else -> {
//            binding.saveChildrenButton.isEnabled = true
//        }

        binding.addChildButton.setOnClickListener {
            childrenCardList.add(childCard)
            childrenRecyclerAdapter.notifyItemInserted(childrenCardList.size - 1)
        }

        if (isFromParentHome == true || isFromParentProfile == true) {
            binding.bottomBar.visibility = View.VISIBLE
            binding.topAppBar.visibility = View.VISIBLE

            if (isFromParentProfile == true) {
                binding.addChildAppBar.title = "Мои дети"
            }

            binding.saveChildrenButton.setOnClickListener {
                val parentId = auth.currentUser?.uid
                if (parentId != null) {
//                    val childrenNamesList = childrenRecyclerAdapter.retrieveChildrenNames()
//                    viewModel.saveChildren(parentId, childrenNamesList.values.toList())
//                    navController.navigate(R.id.parentHomeFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}