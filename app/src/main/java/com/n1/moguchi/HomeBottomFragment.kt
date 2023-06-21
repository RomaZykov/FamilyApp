package com.n1.moguchi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.n1.moguchi.databinding.FragmentHomeBottomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeBottomFragment : Fragment() {

    private lateinit var binding: FragmentHomeBottomBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkIsAuthUser()
    }

    private fun checkIsAuthUser() {
        val authUser = auth.currentUser
        if (authUser != null) {
            binding.email.text = authUser.email
        } else {
            Navigation.findNavController(binding.root)
                .navigate(R.id.starterFragment)
        }
    }
}