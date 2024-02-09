package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.ProfileMode

interface AppRepository {

    fun getProfileMode(): ProfileMode

    fun setProfileMode(newMode: ProfileMode): ProfileMode

    suspend fun sendPasswordResetEmail(childId: String)
}