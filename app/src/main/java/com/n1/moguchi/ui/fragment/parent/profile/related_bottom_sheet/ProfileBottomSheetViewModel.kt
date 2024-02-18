package com.n1.moguchi.ui.fragment.parent.profile.related_bottom_sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.interactors.DeleteAllUserDataUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileBottomSheetViewModel @Inject constructor(
    private val deleteAllUserDataUseCase: DeleteAllUserDataUseCase
) : ViewModel() {

    fun deleteAccountWithAllRelatedData(parentId: String) {
        viewModelScope.launch {
            deleteAllUserDataUseCase.deleteAllUserData(parentId)
        }
    }
}
