package com.n1.moguchi.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.local.UserPreferences
import com.n1.moguchi.data.remote.model.ProfileMode
import com.n1.moguchi.data.repositories.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    fun getUserPrefs(): Flow<UserPreferences> {
        return appRepository.getUserPrefs().map {
            it
        }
    }

    fun updateUserPrefs(newMode: ProfileMode) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.updateUserPrefs(newMode, null)
        }
    }
}