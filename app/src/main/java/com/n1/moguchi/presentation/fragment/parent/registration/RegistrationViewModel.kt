package com.n1.moguchi.presentation.fragment.parent.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.ProfileMode
import com.n1.moguchi.domain.repositories.AppRepository
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
