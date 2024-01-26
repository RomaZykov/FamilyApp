package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.ProfileMode

interface AppSettingsRepository {

    fun getProfileMode(): ProfileMode

    fun setProfileMode(newMode: ProfileMode): ProfileMode
}