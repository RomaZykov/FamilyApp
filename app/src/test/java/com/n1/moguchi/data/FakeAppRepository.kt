package com.n1.moguchi.data

import com.n1.moguchi.data.models.local.UserPreferences
import com.n1.moguchi.data.models.remote.ProfileMode
import com.n1.moguchi.data.repositories.AppRepository
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