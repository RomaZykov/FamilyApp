package com.n1.moguchi.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivityViewModel() : ViewModel() {

    private val auth: FirebaseAuth? = null
    fun isAuthenticated(): Boolean {
        return TODO()
    }
}