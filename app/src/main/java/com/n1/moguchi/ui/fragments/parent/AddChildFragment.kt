package com.n1.moguchi.ui.fragments.parent

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.databinding.FragmentAddChildBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.adapters.ChildrenRecyclerAdapter
import com.n1.moguchi.ui.viewmodels.AddChildViewModel
import javax.inject.Inject

class AddChildFragment : Fragment() {

    private var _binding: FragmentAddChildBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var childrenRecyclerAdapter: ChildrenRecyclerAdapter

//    private val childrenList: MutableList<Child> = mutableListOf()

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
        val parentId = auth.currentUser?.uid

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val isFromParentHome = arguments?.getBoolean("isFromParentHome")
        val isFromParentProfile = arguments?.getBoolean("isFromParentProfile")

        if (parentId != null) {
            viewModel.getChildren(parentId)
        }

        val recyclerView: RecyclerView = binding.rvChildrenList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        childrenRecyclerAdapter = ChildrenRecyclerAdapter()
        recyclerView.adapter = childrenRecyclerAdapter

        viewModel.children.observe(viewLifecycleOwner) {
            val child = Child()
            if (it.isEmpty()) {
                viewModel.createNewChild("0", child)
                childrenRecyclerAdapter.children.add(0, child)
                childrenRecyclerAdapter.notifyItemInserted(0)
                childrenRecyclerAdapter.notifyItemChanged(childrenRecyclerAdapter.itemCount - 1)
            }
            childrenRecyclerAdapter.onNewChildAddClicked = {
                viewModel.createNewChild("0", Child())
                childrenRecyclerAdapter.children = it.toMutableList()
                childrenRecyclerAdapter.notifyItemInserted(it.size - 1)
                childrenRecyclerAdapter.notifyItemChanged(childrenRecyclerAdapter.itemCount - 1)
            }
            childrenRecyclerAdapter.onChildDeleteClicked = {
                viewModel.deleteChildProfile()
            }
            Log.d("AddChildFragment", "Children = $it, count = ${it.size}")
        }


        if (isFromParentHome == true || isFromParentProfile == true) {
            binding.bottomBar.visibility = View.VISIBLE
            binding.topAppBar.visibility = View.VISIBLE

            if (isFromParentProfile == true) {
                binding.addChildAppBar.title = "Мои дети"
            }

            binding.saveChildrenButton.setOnClickListener {
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