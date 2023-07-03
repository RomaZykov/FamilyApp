package com.n1.moguchi.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivityViewModel(private val myRepository: MyRepository) : ViewModel() {

    private val auth: FirebaseAuth? = null
    fun isAuthenticated(): Boolean {
        return TODO()
    }
}