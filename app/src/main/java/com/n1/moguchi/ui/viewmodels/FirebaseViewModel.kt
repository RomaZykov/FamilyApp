package com.n1.moguchi.ui.viewmodels

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseViewModel {
    private val database = Firebase.database
    private val dbRootRef = database.reference
    private val parentNode = dbRootRef.child("parents")
}