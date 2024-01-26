package com.n1.moguchi.data.impl

import com.n1.moguchi.data.models.ProfileMode
import com.n1.moguchi.data.repositories.AppSettingsRepository
import javax.inject.Inject

class AppSettingsRepositoryImpl @Inject constructor() : AppSettingsRepository {

    private var profileMode: ProfileMode = ProfileMode.PARENT_MODE

    override fun getProfileMode(): ProfileMode {
        return profileMode
    }

    override fun setProfileMode(newMode: ProfileMode): ProfileMode {
        profileMode = newMode
        return profileMode
    }
}