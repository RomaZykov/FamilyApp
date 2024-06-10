package com.n1.moguchi.data.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.n1.moguchi.data.local.PreferencesKeys
import com.n1.moguchi.data.local.UserPreferences
import com.n1.moguchi.data.remote.model.Child
import com.n1.moguchi.data.remote.model.Parent
import com.n1.moguchi.data.remote.model.ProfileMode
import com.n1.moguchi.domain.repositories.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    database: FirebaseDatabase,
    private val dataStore: DataStore<Preferences>,
    private val auth: FirebaseAuth,
) : AppRepository {

    private val childrenRef: DatabaseReference = database.getReference("children")
    private val parentsRef: DatabaseReference = database.getReference("parents")

    override fun getUserPrefs(): Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val currentProfileMode =
                preferences[PreferencesKeys.CURRENT_PROFILE_MODE] ?: "undefined"
            val currentChildId = preferences[PreferencesKeys.CURRENT_CHILD_ID] ?: "undefined"
            UserPreferences(currentProfileMode, currentChildId)
        }

    override suspend fun updateUserPrefs(newMode: ProfileMode, childId: String?) {
        dataStore.edit { preferences ->
            if (childId != null) {
                preferences[PreferencesKeys.CURRENT_CHILD_ID] = childId
            }
            when (newMode) {
                ProfileMode.PARENT_MODE -> {
                    preferences[PreferencesKeys.CURRENT_PROFILE_MODE] = "parent_mode"
                }

                ProfileMode.CHILD_MODE -> {
                    preferences[PreferencesKeys.CURRENT_PROFILE_MODE] = "child_mode"
                }

                ProfileMode.UNDEFINED -> {
                    preferences[PreferencesKeys.CURRENT_PROFILE_MODE] = "undefined"
                }
            }
        }
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
                                    .setAndroidPackageName(APP_PACKAGE_NAME, false, null)
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

    companion object {
        private const val APP_PACKAGE_NAME = "com.n1.moguchi"
    }
}
