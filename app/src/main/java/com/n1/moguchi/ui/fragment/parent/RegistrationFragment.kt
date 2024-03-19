package com.n1.moguchi.ui.fragment.parent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private lateinit var navController: NavController

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

        navController = findNavController()

        auth = Firebase.auth

        binding.googleLoginButton.setOnClickListener {
            signIn()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signIn() {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(getString(R.string.web_client_id))
            .setFilterByAuthorizedAccounts(true)
//            .setNonce(<nonce string to use when generating a Google ID token>)
            .build()

        val credentialManager = CredentialManager.create(requireContext())
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = requireContext()
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
//                handleFailure(e)
                Log.e(
                    "RegistrationFragment",
                    "Invalid credentialManager $e",
                    e
                )
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(
                            "RegistrationFragment",
                            "Received an invalid google id token response",
                            e
                        )
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e("RegistrationFragment", "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e("RegistrationFragment", "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(googleIdTokenCredential: GoogleIdTokenCredential) {
        val idToken = googleIdTokenCredential.idToken
        val firebaseCredential =
            GoogleAuthProvider.getCredential(
                idToken,
                null
            )
        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    val rootRef = FirebaseDatabase.getInstance().reference
                    val uidRef = rootRef.child("parents").child(uid!!)
                    val eventListener: ValueEventListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                val email = task.result.user?.email
                                saveParentToFirebase(email = email.toString())
                                val action =
                                    RegistrationFragmentDirections.actionRegistrationFragmentToOnBoardingParentFragment()
                                navController.navigate(action)
                            } else {
                                val action =
                                    RegistrationFragmentDirections.actionRegistrationFragmentToHomeFragment()
                                navController.navigate(action)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            //Don't ignore potential errors!
                            Log.d("TAG", error.message)
                        }
                    }
                    uidRef.addListenerForSingleValueEvent(eventListener)
                } else {
                    Log.w("RegistrationFragment", "signInWithCredential:failure", task.exception)
                    // TODO - show warning
                }
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