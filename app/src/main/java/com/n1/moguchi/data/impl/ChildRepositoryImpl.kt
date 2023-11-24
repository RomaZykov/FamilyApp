package com.n1.moguchi.data.impl

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.data.repositories.ChildRepository
import javax.inject.Inject

class ChildRepositoryImpl @Inject constructor() : ChildRepository {

    private val database = Firebase.database
    val parentRef = database.getReference("parents/")

}