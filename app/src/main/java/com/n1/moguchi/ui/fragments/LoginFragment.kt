package com.n1.moguchi.ui.fragments

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.n1.moguchi.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.R

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oneTapClient = Identity.getSignInClient(requireActivity())
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    try {
                        val credential = oneTapClient.getSignInCredentialFromIntent(data)
                        val idToken = credential.googleIdToken
                        when {
                            idToken != null -> {
                                Log.d(TAG, "Got ID token.")
                                firebaseAuthWithGoogle(idToken)
                            }
                            else -> {
                                // Shouldn't happen.
                                Log.d(TAG, "No ID token or password!")
                            }
                        }
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
            }

        binding.buttonSignIn.setOnClickListener {
            signIn()
        }

        binding.googleLoginButton.setOnClickListener {
            logInWithGoogle(resultLauncher)
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // reload()
        }
    }

    private fun logInWithGoogle(resultLauncher: ActivityResultLauncher<IntentSenderRequest>) {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(requireActivity()) { result ->
                val intentSender =
                    IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                resultLauncher.launch(intentSender)
            }
            .addOnFailureListener(requireActivity()) { e ->
                e.localizedMessage?.let { Log.d(TAG, it) }
            }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(
                        TAG,
                        "signInWithCredential:success"
                    )
                    val user = auth.currentUser
                    val intent = Intent(requireActivity(), HomeFragment::class.java)
                    startActivity(intent)
                } else {
                    Log.w(
                        TAG,
                        "signInWithCredential:failure",
                        task.exception
                    )
                }
            }
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

                    val intent = Intent(context, HomeFragment::class.java)
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