package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.local.UserPreferences
import com.n1.moguchi.data.models.remote.ProfileMode
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    fun getUserPrefs(): Flow<UserPreferences>

    suspend fun updateUserPrefs(newMode: ProfileMode,  childId: String?)

    suspend fun sendChildPasswordNotificationEmail(childId: String)
}