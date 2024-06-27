package com.n1.moguchi.ui.fragment.parent.profile.related_bottom_sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.ProfileMode
import com.n1.moguchi.domain.repositories.AppRepository
import com.n1.moguchi.domain.usecases.DeleteAllUserDataUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileBottomSheetViewModel @Inject constructor(
    private val deleteAllUserDataUseCase: DeleteAllUserDataUseCase,
    private val appRepository: AppRepository
) : ViewModel() {

    fun deleteAccountWithAllRelatedData(parentId: String) {
        viewModelScope.launch {
            deleteAllUserDataUseCase.deleteAllUserData(parentId)
        }
    }

    fun updateUserPrefs(newMode: ProfileMode) {
        viewModelScope.launch {
            appRepository.updateUserPrefs(newMode, null)
        }
    }
}
