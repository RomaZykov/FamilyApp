package com.example.moguchi.ui.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.moguchi.databinding.FragmentLoginBinding
import com.example.moguchi.ui.activities.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignIn.setOnClickListener {
            signIn()
        }

        auth = Firebase.auth
    }

    private fun signIn() {
        binding.buttonSignIn.isEnabled = false

        val email = binding.emailEditText.editText?.text.toString()
        val password = binding.passwordEditText.editText?.text.toString()

        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")

                    binding.buttonSignIn.isEnabled = true
                    binding.buttonSignIn.alpha = 1.0f

                    val user = auth.currentUser
                    Toast.makeText(
                        context,
                        "Sign-in: ${user?.email}",
                        Toast.LENGTH_SHORT,
                    ).show()

                    val intent = Intent(context, HomeActivity::class.java)
                    startActivity(intent)

                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)

                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }

//                if (!task.isSuccessful) {
//                    binding.status.setText(R.string.auth_failed)
//                }
            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = binding.emailEditText.editText?.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.emailEditText.error = "Required."
            valid = false
        } else {
            binding.emailEditText.error = null
        }

        val password = binding.passwordEditText.editText?.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.passwordEditText.error = "Required."
            valid = false
        } else {
            binding.passwordEditText.error = null
        }

        return valid
    }
}