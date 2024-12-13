package com.example.familyapp.domain.repositories

import com.example.familyapp.data.local.UserPreferences
import com.example.familyapp.data.ProfileMode
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    fun getUserPrefs(): Flow<UserPreferences>

    suspend fun updateUserPrefs(newMode: ProfileMode, childId: String?)

    suspend fun sendChildPasswordNotificationEmail(childId: String)
}