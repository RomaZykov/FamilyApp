package com.n1.moguchi.ui.fragment.parent.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.remote.model.ProfileMode
import com.n1.moguchi.data.repositories.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    fun updateUserPrefs(newMode: ProfileMode) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.updateUserPrefs(newMode, null)
        }
    }
}
