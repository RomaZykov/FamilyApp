package com.n1.moguchi.data.Implementations

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.data.repositories.ChildRepository

class ChildRepositoryImpl : ChildRepository {

    private val database = Firebase.database
    val parentRef = database.getReference("parents/")

}