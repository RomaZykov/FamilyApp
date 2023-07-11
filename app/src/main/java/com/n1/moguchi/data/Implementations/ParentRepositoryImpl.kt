package com.n1.moguchi.data.Implementations

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.n1.moguchi.data.repositories.ParentRepository
import com.n1.moguchi.data.models.Child

class ParentRepositoryImpl(database: FirebaseDatabase) : ParentRepository {

    private val childrenRef: DatabaseReference = database.getReference("children")

    override fun getChildrenList(parentId: String): List<Child> {
        val childrenRefByParentId = childrenRef.child(parentId)
        val children = mutableListOf<Child>()
        childrenRefByParentId.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                children.addAll(snapshot.children.map { dataSnapshot ->
                    dataSnapshot.getValue(Child::class.java)!!
                })
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return children
    }

    override fun getChild(parentId: String, childId: String): Child {
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
        childRefByParentId.addValueEventListener(childListener)
        return child[0]
    }

    override fun deleteChild(parentId: String, childId: String) {
        TODO("Not yet implemented")
    }
}