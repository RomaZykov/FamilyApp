package com.n1.moguchi.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentAddChildBinding
import com.n1.moguchi.data.models.Child
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.UUID

class AddChildFragment : Fragment() {

    private lateinit var binding: FragmentAddChildBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            saveChildToFirebase()
        }
    }

    private fun saveChildToFirebase() {
        auth = Firebase.auth

        val childName = binding.childName.editText?.text.toString().trim { it <= ' ' }
        val childYears = binding.years.editText?.text.toString()

        val database =
            FirebaseDatabase.getInstance("https://moguchi-app-default-rtdb.europe-west1.firebasedatabase.app/")
        val parentId = auth.currentUser?.uid

        if (childName.isBlank() || childYears.isBlank()) {
            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val childRef = database.reference.child("children")
        val childId = UUID.randomUUID().toString()
        Log.d("AddChildFragment", "ChildRef - $childRef")
        val child = Child(
            childId = childId,
            parentOwnerId = parentId.toString(),
            childName = childName,
            years = childYears.toInt()
        )
        childRef.child(childId).setValue(child)

        Navigation.findNavController(binding.root)
            .navigate(R.id.action_addChildFragment_to_onBoardingFragment)
    }
}