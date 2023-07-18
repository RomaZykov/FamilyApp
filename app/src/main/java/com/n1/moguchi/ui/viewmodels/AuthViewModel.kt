package com.n1.moguchi.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class AuthViewModel @Inject constructor() : ViewModel() {

    private var auth: FirebaseAuth? = null

    init {
        auth = Firebase.auth
    }
}