package com.n1.moguchi.data.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.Parent
import com.n1.moguchi.data.repositories.ParentRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ParentRepositoryImpl @Inject constructor(
    database: FirebaseDatabase,
    private val auth: FirebaseAuth
) : ParentRepository {

    private val childrenRef: DatabaseReference = database.getReference("children")
    private val parentsRef: DatabaseReference = database.getReference("parents")

    override suspend fun fetchChildren(parentId: String): Map<String, Child> {
        val childrenRefByParentId = childrenRef.orderByChild("parentOwnerId").equalTo(parentId)
        val children: MutableMap<String, Child> = mutableMapOf()
        childrenRefByParentId.get().await().children.map {
            children.put(it?.key!!, it.getValue(Child::class.java)!!)
        }
        return children
    }

    override fun getAndSaveChildToDb(parentId: String, childUser: Child): Child {
        val newChildRef: DatabaseReference = childrenRef.push()
        val childId: String? = newChildRef.key
        val newChild = childUser.copy(
            childId = childId,
            parentOwnerId = parentId
        )
        newChildRef.setValue(newChild)
        return newChild
    }

    override fun getAndUpdateChildInDb(parentId: String, childUser: Child): Child {
        val specificChild = childrenRef.child(childUser.childId!!)
        val updatedChild = childUser.copy(
            childName = childUser.childName,
            imageResourceId = childUser.imageResourceId
        )
        val childValues = updatedChild.toMap()
        specificChild.updateChildren(childValues)
        return updatedChild
    }

    override suspend fun deleteChildProfile(parentId: String, childId: String) {
        val childRefByParentId = childrenRef.child(childId)
        childRefByParentId.removeValue()
    }

    override suspend fun setPassword(password: Int, childId: String) {
        val parentId = auth.currentUser?.uid

        val specificChild =
            childrenRef.child(childId).get().await().getValue(Child::class.java)
        specificChild?.passwordFromParent = password
        val updatedChild = specificChild?.toMap()
        val childValues = updatedChild?.toMap()
        childrenRef.child(childId).updateChildren(childValues!!)

        val specificParent = parentsRef.child(parentId!!).get().await().getValue(Parent::class.java)
        specificParent?.childrenPasswordsMap?.put(password, specificChild)
        val updatedParent = specificParent?.toMap()
        val parentValues = updatedParent?.toMap()
        parentsRef.child(parentId).updateChildren(parentValues!!)
    }

    override fun checkPassword(password: Int, childId: String): Flow<Boolean> = callbackFlow {
        val listener = childrenRef.orderByChild("childId").equalTo(childId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (child in snapshot.children) {
                            val childFromDb = child.getValue(Child::class.java)
                            if (childFromDb != null && password == childFromDb.passwordFromParent) {
                                trySend(true)
                            } else {
                                trySend(false)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        awaitClose { childrenRef.removeEventListener(listener) }
    }
}