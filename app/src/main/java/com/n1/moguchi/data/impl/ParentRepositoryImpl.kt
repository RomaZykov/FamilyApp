package com.n1.moguchi.data.impl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.Parent
import com.n1.moguchi.data.repositories.ParentRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ParentRepositoryImpl @Inject constructor(
    database: FirebaseDatabase,
    private val auth: FirebaseAuth
) : ParentRepository {

    private val childrenRef: DatabaseReference = database.getReference("children")
    private val parentsRef: DatabaseReference = database.getReference("parents")

    override suspend fun getChildren(parentId: String): Map<String, Child> {
        val childrenRefByParentId = childrenRef.child(parentId)
        val children: MutableMap<String, Child> = mutableMapOf()
        childrenRefByParentId.get().await().children.map {
            children.put(it?.key!!, it.getValue(Child::class.java)!!)
        }
        return children
    }

    override suspend fun getChild(parentId: String, childId: String): Child {
        val childRefByParentId = childrenRef.child(parentId).child(childId)
        val child = mutableListOf<Child>()
        val childListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    child.add(snapshot.getValue<Child>()!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        childRefByParentId.addListenerForSingleValueEvent(childListener)
        return child[0]
    }

    override fun getAndSaveChildToDb(parentId: String, childUser: Child): Child {
        val childrenRefByParentId = childrenRef.child(parentId)
        val newChildRef: DatabaseReference = childrenRefByParentId.push()
        val childId: String? = newChildRef.key
        val newChild = childUser.copy(
            childId = childId,
            parentOwnerId = parentId
        )
        newChildRef.setValue(newChild)
        Log.d("ParentRepositoryImpl", "$newChildRef")
        return newChild
    }

    override fun getAndUpdateChildInDb(parentId: String, childUser: Child): Child {
        val specificChild = childrenRef.child(parentId).child(childUser.childId!!)
        val updatedChild = childUser.copy(
            childName = childUser.childName,
            imageResourceId = childUser.imageResourceId
        )
        val childValues = updatedChild.toMap()
        specificChild.updateChildren(childValues)
        return updatedChild
    }

    override suspend fun setPassword(password: Int, childId: String) {
        val parentId = auth.currentUser?.uid

        val specificChild =
            childrenRef.child(parentId!!).child(childId).get().await().getValue(Child::class.java)
        specificChild?.passwordFromParent = password
        val updatedChild = specificChild?.toMap()
        val childValues = updatedChild?.toMap()
        childrenRef.child(parentId).child(childId).updateChildren(childValues!!)

        val specificParent = parentsRef.child(parentId).get().await().getValue(Parent::class.java)
        specificParent?.childrenPasswordsMap?.put(password, specificChild)
        val updatedParent = specificParent?.toMap()
        val parentValues = updatedParent?.toMap()
        parentsRef.child(parentId).updateChildren(parentValues!!)
    }

    override suspend fun deleteChildProfile(parentId: String, childId: String) {
        val childRefByParentId = childrenRef.child(parentId).child(childId)
        childRefByParentId.removeValue()
    }
}