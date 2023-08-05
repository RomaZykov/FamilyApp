package com.n1.moguchi.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
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
import com.n1.moguchi.ui.adapters.ChildrenListAdapter
import com.n1.moguchi.ui.viewmodels.AddChildViewModel
import java.util.UUID
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

        binding.nextButton.setOnClickListener {
            saveChildrenToFirebase()
        }
    }

    private fun saveChildrenToFirebase() {
        val parentId = auth.currentUser?.uid
        if (parentId != null) {
            val childrenCount = childrenListAdapter.itemCount
            for (position in 0..childrenCount) {
                val childName = childrenListAdapter.getItem(position)
                val childId = UUID.randomUUID().toString()
                val child = Child(
                    childId = childId,
                    parentOwnerId = parentId.toString(),
                    childName = childName
                )
                viewModel.addChild(parentId, child)
            }
        }
        Navigation.findNavController(binding.root).navigate(R.id.homeFragment)
    }
}