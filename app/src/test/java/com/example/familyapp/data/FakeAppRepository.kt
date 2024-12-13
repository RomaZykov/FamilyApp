package com.example.familyapp.data

import com.example.familyapp.data.local.UserPreferences
import com.example.familyapp.domain.repositories.AppRepository
import kotlinx.coroutines.flow.Flow

class FakeAppRepository : AppRepository {
    override fun getUserPrefs(): Flow<UserPreferences> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserPrefs(newMode: ProfileMode, childId: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun sendChildPasswordNotificationEmail(childId: String) {
        TODO("Not yet implemented")
    }
}