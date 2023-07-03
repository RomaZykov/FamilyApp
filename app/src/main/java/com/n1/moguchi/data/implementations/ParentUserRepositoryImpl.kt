package com.n1.moguchi.data.implementations

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.domain.repositories.ParentUserRepository

class ParentUserRepositoryImpl : ParentUserRepository {
    val database = Firebase.database
}