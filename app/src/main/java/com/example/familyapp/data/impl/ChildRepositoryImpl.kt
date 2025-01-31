package com.example.familyapp.data.impl

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.familyapp.data.remote.model.Child
import com.example.familyapp.domain.repositories.ChildRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ChildRepositoryImpl @Inject constructor(
    database: FirebaseDatabase
) : ChildRepository {

    private val childrenRef: DatabaseReference = database.getReference("children")

    override fun fetchChildData(childId: String): Flow<Child?> = callbackFlow {
        val listener = childrenRef.orderByChild("childId").equalTo(childId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (child in snapshot.children) {
                            trySend(child.getValue(Child::class.java))
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        awaitClose { childrenRef.removeEventListener(listener) }
    }
}