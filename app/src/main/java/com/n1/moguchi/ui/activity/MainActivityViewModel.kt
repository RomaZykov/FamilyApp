package com.n1.moguchi.ui.activity

import androidx.lifecycle.ViewModel
import com.n1.moguchi.data.models.ProfileMode
import com.n1.moguchi.data.repositories.AppSettingsRepository
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) : ViewModel() {

    private var profileMode: ProfileMode? = null

//    val isAuthenticated: Boolean
//        get() = hostConfig.hasAuthentication()

    fun getProfileMode(): ProfileMode {
        return appSettingsRepository.getProfileMode()
    }

    fun setProfileMode(newMode: ProfileMode) {
        profileMode = appSettingsRepository.setProfileMode(newMode)
    }
}