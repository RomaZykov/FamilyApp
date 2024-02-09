package com.n1.moguchi.ui.activity

import androidx.lifecycle.ViewModel
import com.n1.moguchi.data.models.ProfileMode
import com.n1.moguchi.data.repositories.AppRepository
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    private var profileMode: ProfileMode? = null

//    val isAuthenticated: Boolean
//        get() = hostConfig.hasAuthentication()

    fun getProfileMode(): ProfileMode {
        return appRepository.getProfileMode()
    }

    fun setProfileMode(newMode: ProfileMode) {
        profileMode = appRepository.setProfileMode(newMode)
    }
}