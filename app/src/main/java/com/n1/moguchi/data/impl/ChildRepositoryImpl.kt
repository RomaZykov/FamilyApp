package com.n1.moguchi.data.impl

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.repositories.ChildRepository
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ChildRepositoryImpl @Inject constructor(
    database: FirebaseDatabase
) : ChildRepository {

    private val childrenRef: DatabaseReference = database.getReference("children")

    override suspend fun fetchChildData(childId: String): Flow<Child?> {
        val childRefByParentId = childrenRef.orderByChild("childId").equalTo(childId)
//        val child = childRefByParentId.get().await().getValue(Child::class.java)
        return childRefByParentId.addValueEventListenerFlow(Child::class.java)
    }

    private fun <T> Query.addValueEventListenerFlow(dataType: Class<T>): Flow<T?> =
        callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val child = dataSnapshot.getValue(dataType)
                    trySend(child)
                }

                override fun onCancelled(error: DatabaseError) {
                    cancel()
                }
            }
            addValueEventListener(listener)
            awaitClose { removeEventListener(listener) }
        }
}