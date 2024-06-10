package com.n1.moguchi.domain.repositories

import com.n1.moguchi.data.local.UserPreferences
import com.n1.moguchi.data.remote.model.ProfileMode
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    fun getUserPrefs(): Flow<UserPreferences>

    suspend fun updateUserPrefs(newMode: ProfileMode, childId: String?)

    suspend fun sendChildPasswordNotificationEmail(childId: String)
}