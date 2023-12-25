package com.n1.moguchi.data.impl

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.data.repositories.ChildRepository
import javax.inject.Inject

class ChildRepositoryImpl @Inject constructor(
    database: FirebaseDatabase
) : ChildRepository {

    val parentRef = database.getReference("parents")
}