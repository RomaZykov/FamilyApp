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
import com.n1.moguchi.domain.models.child.Child
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

        binding.addButton.apply {
            isEnabled = true
            setOnClickListener {
                saveChildToFirebase()
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_addChildFragment_to_onBoardingFragment)
            }
        }
    }

    private fun saveChildToFirebase() {
        auth = Firebase.auth

        val database =
            FirebaseDatabase.getInstance("https://moguchi-app-default-rtdb.europe-west1.firebasedatabase.app/")
        val childId = UUID.randomUUID().toString()

        val childFirstName = binding.firstName.editText?.text.toString().trim { it <= ' ' }
        val childLastName = binding.lastName.editText?.text.toString().trim { it <= ' ' }
        val childYears = binding.years.editText?.text.toString()

        if (childFirstName.isEmpty() || childLastName.isEmpty() || childYears.isEmpty()) {
            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val parentId = auth.currentUser?.uid
        val parentRef = database.getReference("parents").child(parentId!!)
        val child = Child(
            firstName = childFirstName,
            lastName = childLastName,
            years = childYears.toInt()
        )
        parentRef.child("children").child(childId).setValue(child)

        Log.d("AddChildFragment", "Id for parent - $parentId")
    }
}