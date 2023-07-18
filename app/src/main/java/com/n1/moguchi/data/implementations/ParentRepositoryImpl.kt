package com.n1.moguchi.data.implementations

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.n1.moguchi.data.repositories.ParentRepository
import com.n1.moguchi.data.models.Child
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ParentRepositoryImpl @Inject constructor() : ParentRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val childrenRef: DatabaseReference = database.getReference("children")

    override suspend fun getChildrenList(parentId: String): List<Child> {
        val childrenRefByParentId = childrenRef.child(parentId)
        val childrenList: MutableList<Child> = mutableListOf()
        childrenRefByParentId.get().await().children.map {
            childrenList.add(it.getValue(Child::class.java)!!)
        }
        return childrenList
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
                TODO("Not yet implemented")
            }
        }
        childRefByParentId.addListenerForSingleValueEvent(childListener)
        return child[0]
    }

    override fun saveChild(parentId: String, child: Child) {
        val childrenRefByParentId = childrenRef.child(parentId)
        childrenRefByParentId.child(child.childId!!).setValue(child)
    }

    override fun deleteChild(parentId: String, childId: String) {
        TODO("Not yet implemented")
    }
}