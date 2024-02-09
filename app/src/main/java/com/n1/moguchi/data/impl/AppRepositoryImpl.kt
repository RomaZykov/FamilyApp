package com.n1.moguchi.data.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.Parent
import com.n1.moguchi.data.models.ProfileMode
import com.n1.moguchi.data.repositories.AppRepository
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    database: FirebaseDatabase,
    private val auth: FirebaseAuth
) : AppRepository {

    private val childrenRef: DatabaseReference = database.getReference("children")
    private val parentsRef: DatabaseReference = database.getReference("parents")
    private var profileMode: ProfileMode = ProfileMode.PARENT_MODE

    override fun getProfileMode(): ProfileMode {
        return profileMode
    }

    override fun setProfileMode(newMode: ProfileMode): ProfileMode {
        profileMode = newMode
        return profileMode
    }

    override suspend fun sendPasswordResetEmail(childId: String) {
        var parentId = ""
        childrenRef.child(childId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val child = snapshot.getValue(Child::class.java)
                if (child != null) {
                    parentId = child.parentOwnerId.toString()
                }
                parentsRef.child(parentId).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val parent = snapshot.getValue(Parent::class.java)
                            if (parent != null) {
                                TODO("Use cloud messaging instead")
//                                auth.sendPasswordResetEmail(parent.email!!)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}