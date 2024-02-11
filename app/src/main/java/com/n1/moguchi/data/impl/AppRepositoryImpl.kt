package com.n1.moguchi.data.impl

import com.google.firebase.auth.ActionCodeSettings
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
    private val auth: FirebaseAuth,
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

    override suspend fun sendChildPasswordNotificationEmail(childId: String) {
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
                            val url =
                                "https://firebase.google.com/docs/auth/android/passing-state-in-email-actions#passing_statecontinue_url_in_email_actions"
                            if (parent != null) {
                                val actionCodeSettings = ActionCodeSettings.newBuilder()
                                    .setUrl(url)
                                    .setAndroidPackageName("com.n1.moguchi", false, null)
                                    .build()

                                auth.sendPasswordResetEmail(parent.email!!, actionCodeSettings)
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