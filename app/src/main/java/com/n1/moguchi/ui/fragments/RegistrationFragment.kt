package com.n1.moguchi.ui.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentRegistrationBinding
import com.n1.moguchi.domain.models.Parent

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registrationButton.setOnClickListener { registerUser() }
//        binding.googleLoginButton.setOnClickListener {  }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // reload()
        }
    }

    private fun registerUser() {

        val userName: String = binding.firstName.editText?.text.toString().trim { it <= ' ' }
        val email: String = binding.email.editText?.text.toString().trim { it <= ' ' }
        val password: String = binding.password.editText?.text.toString().trim { it <= ' ' }
        val role: String = binding.status.editText?.text.toString().trim { it <= ' ' }

        if (userName.isBlank() || email.isBlank() || password.isBlank() || role.isBlank()) {
            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        saveParentToFirebase(userName, email, password, role)
                        Toast.makeText(context, "${user?.email}", Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_registrationFragment_to_addChildFragment)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)

                        Toast.makeText(
                            context,
                            "Registration failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }

    private fun saveParentToFirebase(
        userName: String,
        email: String,
        password: String,
        role: String
    ) {
        val database =
            FirebaseDatabase.getInstance("https://moguchi-app-default-rtdb.europe-west1.firebasedatabase.app/")
        val parentsRef = database.getReference("parents")
        val parentId = auth.currentUser?.uid

        val passwordHash= BCrypt.withDefaults().hashToString(12, password.toCharArray())

        val parent = Parent(
            userName = userName,
            email = email,
            passwordHash = passwordHash,
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