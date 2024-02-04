package com.n1.moguchi.data.impl

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.repositories.ChildRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChildRepositoryImpl @Inject constructor(
    database: FirebaseDatabase
) : ChildRepository {

    private val childrenRef: DatabaseReference = database.getReference("children")

    override suspend fun getChild(childId: String): Flow<Child> = flow {
        val childRefByParentId = childrenRef.orderByChild("childId").equalTo(childId)
        val child = childRefByParentId.get().await().getValue(Child::class.java)
        emit(child!!)
    }
}