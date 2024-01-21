package com.n1.moguchi.ui.fragment.parent

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Parent
import com.n1.moguchi.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var signInClient: SignInClient

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            handleSignInResult(result.data, result.resultCode)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInClient = Identity.getSignInClient(requireActivity())
        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser == null) {
            oneTapSignIn()
        }

        binding.googleLoginButton.setOnClickListener {
            signIn()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleSignInResult(data: Intent?, resultCode: Int) {
        if (resultCode == Activity.RESULT_OK) {
            try {
                val credential = signInClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken
                when {
                    idToken != null -> {
                        Log.d("RegistrationFragment", "RegistrationFragment: ${credential.id}")
                        firebaseAuthWithGoogle(idToken)
                    }

                    else -> {
                        Log.d("RegistrationFragment", "No ID token!")
                    }
                }
            } catch (e: IntentSender.SendIntentException) {
                Log.e("RegistrationFragment", "Couldn't start One Tap UI: ${e.localizedMessage}")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("RegistrationFragment", "signInWithCredential:success")
                    val uid = auth.currentUser?.uid
                    val rootRef = FirebaseDatabase.getInstance().reference
                    val uidRef = rootRef.child("parents").child(uid!!)
                    val eventListener: ValueEventListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                val email = task.result.user?.email
                                saveParentToFirebase(email = email.toString())
                                Navigation.findNavController(binding.root)
                                    .navigate(R.id.action_registrationFragment_to_onBoardingParentFragment)
                            } else {
                                Navigation.findNavController(binding.root)
                                    .navigate(R.id.action_registrationFragment_to_homeFragment)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d("TAG", error.message) //Don't ignore potential errors!
                        }
                    }
                    uidRef.addListenerForSingleValueEvent(eventListener)
                } else {
                    Log.w("RegistrationFragment", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun signIn() {
        val signInRequest = GetSignInIntentRequest.builder()
            .setServerClientId(getString(R.string.web_client_id))
            .build()

        signInClient.getSignInIntent(signInRequest)
            .addOnSuccessListener { pendingIntent ->
                launchSignIn(pendingIntent)
            }
            .addOnFailureListener { e ->
                Log.e("RegistrationFragment", "Google Sign-in failed", e)
            }
    }

    private fun oneTapSignIn() {
        val oneTapRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
//            .setAutoSelectEnabled(true)
            .build()

        signInClient.beginSignIn(oneTapRequest)
            .addOnSuccessListener { result ->
                launchSignIn(result.pendingIntent)
            }
            .addOnFailureListener { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
            }
    }

    private fun launchSignIn(pendingIntent: PendingIntent) {
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent).build()
            signInLauncher.launch(intentSenderRequest)
        } catch (e: IntentSender.SendIntentException) {
            Log.e("RegistrationFragment", "Couldn't start Sign In: ${e.localizedMessage}")
        }
    }

    // TODO - Warning - Logic in UI
    private fun saveParentToFirebase(
        email: String
    ) {
        val database = FirebaseDatabase.getInstance(BASE_URL)
        val parentsRef = database.getReference("parents")
        val parentId = auth.currentUser?.uid

        val parent = Parent(
            parentName = "User-${parentId?.slice(0..7)}",
            email = email
        )
        parentsRef.child(parentId!!).setValue(parent)
    }

    companion object {
        private const val BASE_URL =
            "https://moguchi-app-default-rtdb.europe-west1.firebasedatabase.app"
    }
}