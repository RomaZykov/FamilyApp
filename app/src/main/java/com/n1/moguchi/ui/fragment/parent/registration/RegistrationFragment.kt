package com.n1.moguchi.ui.fragment.parent.registration

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import com.n1.moguchi.BuildConfig
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.data.ProfileMode
import com.n1.moguchi.data.remote.model.Parent
import com.n1.moguchi.databinding.FragmentRegistrationBinding
import com.n1.moguchi.ui.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO - Warning - Too many logic in UI and empty catch statements
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[RegistrationViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as MoguchiBaseApplication).appComponent
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
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
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.SERVER_CLIENT_ID)
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
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
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
                                viewModel.updateUserPrefs(ProfileMode.PARENT_MODE)
                                val action =
                                    RegistrationFragmentDirections.actionRegistrationFragmentToHomeFragment()
                                navController.navigate(action)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            //Don't ignore potential errors!
                        }
                    }
                    uidRef.addListenerForSingleValueEvent(eventListener)
                } else {
                    // TODO - show warning
                }
            }
    }

    private fun saveParentToFirebase(
        email: String
    ) {
        // Static function - bad prictice in ui
        val database = FirebaseDatabase.getInstance(BuildConfig.BASE_URL)
        val parentsRef = database.getReference("parents")
        val parentId = auth.currentUser?.uid

        val parent = Parent(
            parentName = "User-${parentId?.slice(0..7)}",
            email = email
        )
        parentsRef.child(parentId!!).setValue(parent)
    }
}