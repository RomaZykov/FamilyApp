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
    private val goalsRef: DatabaseReference = database.getReference("goals")
    private val tasksRef: DatabaseReference = database.getReference("tasks")

    override fun fetchParentData(parentId: String): Flow<Parent?> = callbackFlow {
        val listener = parentsRef.child(parentId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val parent = snapshot.getValue(Parent::class.java)
                        trySend(parent)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        awaitClose { parentsRef.removeEventListener(listener) }
    }

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

    override suspend fun deleteChildProfile(childId: String) {
        val databaseReferences = listOf(goalsRef, childrenRef)
        for (databaseRef in databaseReferences) {
            when (databaseRef) {
                goalsRef -> {
                    var goalId = ""
                    val listener = databaseRef.orderByChild("childOwnerId").equalTo(childId)
                        .addValueEventListener(
                            object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (parameter in snapshot.children) {
                                        if (parameter
                                                .child("childOwnerId").getValue(String::class.java)
                                                .toString() == childId
                                        ) {
                                            goalId = parameter
                                                .child("goalId")
                                                .getValue(String::class.java)
                                                .toString()
                                        }
                                        tasksRef.child(goalId).removeValue()
                                        databaseRef.child(goalId).removeValue()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            }
                        )
                    databaseRef.addValueEventListener(listener)
                }

                childrenRef -> {
                    databaseRef.child(childId).removeValue()
                }
            }
        }
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

    override suspend fun deleteAllUserData(parentId: String) {
        val databaseReferences = listOf(goalsRef, childrenRef, parentsRef)
        for (databaseRef in databaseReferences) {
            when (databaseRef) {
                goalsRef -> {
                    var goalId = ""
                    databaseRef.orderByChild("parentOwnerId").equalTo(parentId)
                        .addValueEventListener(
                            object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (parameter in snapshot.children) {
                                        if (parameter.key == "goalId") {
                                            goalId = parameter
                                                .child("goalId")
                                                .getValue(String::class.java)
                                                .toString()
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            }
                        )
                    tasksRef.child(goalId).removeValue()
                    databaseRef.child(goalId).removeValue()
                }

                childrenRef -> {
                    childrenRef.orderByChild("parentOwnerId").equalTo(parentId)
                        .addValueEventListener(
                            object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    snapshot.ref.removeValue()
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            }
                        )
                }
            }
            databaseRef.child(parentId).removeValue()
        }
    }
}
