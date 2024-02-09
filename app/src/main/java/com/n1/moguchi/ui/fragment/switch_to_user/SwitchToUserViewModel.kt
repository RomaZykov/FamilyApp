package com.n1.moguchi.ui.fragment.switch_to_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.ProfileMode
import com.n1.moguchi.data.repositories.AppRepository
import com.n1.moguchi.data.repositories.ParentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SwitchToUserViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val parentRepository: ParentRepository
) : ViewModel() {

    private val _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    fun getProfileMode(): ProfileMode {
        return appRepository.getProfileMode()
    }

    fun checkPassword(password: Int, childId: String): Flow<Boolean> {
        return parentRepository.checkPassword(password, childId)
    }

    fun resetPassword(childId: String) {
        viewModelScope.launch {
            appRepository.sendPasswordResetEmail(childId)
        }
    }

    fun getChildren(parentId: String) {
        viewModelScope.launch {
            val children: Map<String, Child> = parentRepository.fetchChildren(parentId)
            _children.value = children.values.toList()
        }
    }
}
