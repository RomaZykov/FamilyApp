package com.n1.moguchi.ui.fragments

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentRegistrationBinding
import com.n1.moguchi.data.models.Parent

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signUpRequest: BeginSignInRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oneTapClient = Identity.getSignInClient(requireActivity())
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
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
                                Log.d(TAG, "No ID token!")
                            }
                        }
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
            }

        binding.registrationButton.setOnClickListener { registerUser() }
        binding.googleLoginButton.setOnClickListener { signInWithGoogle(resultLauncher) }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // reload()
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
                    val email = task.result.user?.email
                    val userName = task.result.user?.email
                    val role = task.result.user?.email

//                    saveParentToFirebase(TODO())
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_registrationFragment_to_addChildFragment)
                } else {
                    Log.w(
                        TAG,
                        "signInWithCredential:failure",
                        task.exception
                    )
                }
            }
    }

    private fun signInWithGoogle(resultLauncher: ActivityResultLauncher<IntentSenderRequest>) {
        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener(requireActivity()) { result ->
                val intentSender =
                    IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                resultLauncher.launch(intentSender)
            }
            .addOnFailureListener(requireActivity()) { e ->
                e.localizedMessage?.let { Log.d(TAG, it) }
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
                        val user = auth.currentUser
                        saveParentToFirebase(userName, email, password, role)
                        Toast.makeText(context, "${user?.email}", Toast.LENGTH_SHORT).show()

                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_registrationFragment_to_addChildFragment)
                    } else {
                        Toast.makeText(
                            context, "Registration failed.", Toast.LENGTH_SHORT,
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

        val passwordHash = BCrypt.withDefaults().hashToString(12, password.toCharArray())

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
//                    .navigate(R.id.action_loginRegistrationFragment_to_loginFragment)
                Toast.makeText(context, "Reload successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to reload user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateForm(): Boolean {
        TODO()
    }
}