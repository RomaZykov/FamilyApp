package com.n1.moguchi.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.n1.moguchi.databinding.FragmentAddChildBinding
import com.google.firebase.auth.FirebaseAuth
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.viewmodels.AddChildViewModel
import javax.inject.Inject

class AddChildFragment : Fragment() {

    private lateinit var binding: FragmentAddChildBinding
    private lateinit var auth: FirebaseAuth

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

        binding.nextButton.setOnClickListener {
//            saveChildToFirebase()
        }
    }

//    private fun saveChildToFirebase() {
//        auth = Firebase.auth
//        val parentId = auth.currentUser?.uid
//
//        val childName = binding.childName.editText?.text.toString().trim { it <= ' ' }
//
//        if (childName.isBlank() || childYears.isBlank()) {
//            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val childId = UUID.randomUUID().toString()
//        val child = Child(
//            childId = childId,
//            parentOwnerId = parentId.toString(),
//            childName = childName
//        )
//
//        if (parentId != null) {
//            viewModel.addChild(parentId, child)
//        }
//
//        Navigation.findNavController(binding.root)
//            .navigate(R.id.homeFragment)
//    }
}