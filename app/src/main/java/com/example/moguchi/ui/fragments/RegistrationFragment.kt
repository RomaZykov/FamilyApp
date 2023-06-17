package com.example.moguchi.ui.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.moguchi.R
import com.example.moguchi.api.ApiService
import com.example.moguchi.databinding.FragmentRegistrationBinding
import com.example.moguchi.domain.models.auth.Parent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var api: ApiService
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registrationButton.setOnClickListener { registerUser() }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun registerUser() {

        val firstName: String = binding.firstName.editText?.text.toString().trim { it <= ' ' }
        val lastName: String = binding.lastName.editText?.text.toString().trim { it <= ' ' }
        val email: String = binding.email.editText?.text.toString().trim { it <= ' ' }
        val password: String = binding.password.editText?.text.toString().trim { it <= ' ' }
        val role: String = binding.status.editText?.text.toString().trim { it <= ' ' }

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        } else {
            saveParentToFirebase(firstName, lastName, email, password, role)
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")

                    val user = auth.currentUser
                    Toast.makeText(context, "${user?.email}", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_registrationFragment_to_addChildFragment)

                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)

                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun saveParentToFirebase(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        role: String
    ) {
        val database =
            FirebaseDatabase.getInstance("https://moguchi-app-default-rtdb.europe-west1.firebasedatabase.app/")
        val parentsRef = database.getReference("parents")
        val parentId = auth.currentUser?.uid

        val parent = Parent(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            role = role
        )
        parentsRef.child(parentId!!).setValue(parent)
    }

    private fun reload() {
        auth.currentUser!!.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_loginRegistrationFragment_to_loginFragment)
                Toast.makeText(context, "Reload successful!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "reload", task.exception)
                Toast.makeText(context, "Failed to reload user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateForm(): Boolean {
        TODO()
    }

}