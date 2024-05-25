package com.n1.moguchi.ui.fragment.switch_to_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.local.UserPreferences
import com.n1.moguchi.data.remote.model.Child
import com.n1.moguchi.data.remote.model.ProfileMode
import com.n1.moguchi.data.repositories.AppRepository
import com.n1.moguchi.data.repositories.ParentRepository
import com.n1.moguchi.interactors.FetchChildDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SwitchToUserViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val parentRepository: ParentRepository,
    private val fetchChildDataUseCase: FetchChildDataUseCase
) : ViewModel() {

    private val _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    fun getUserPrefs(): Flow<UserPreferences> {
        return appRepository.getUserPrefs().map {
            it
        }
    }

    fun updateUserPrefs(newMode: ProfileMode, childId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.updateUserPrefs(newMode, childId)
        }
    }

    fun getChild(childId: String): Flow<Child> {
        return fetchChildDataUseCase.invoke(childId).map {
            it!!
        }
    }

    fun checkPassword(password: Int, childId: String): Flow<Boolean> {
        return parentRepository.checkPassword(password, childId)
    }

    fun resetPassword(childId: String) {
        viewModelScope.launch {
            appRepository.sendChildPasswordNotificationEmail(childId)
        }
    }

    fun getChildren(parentId: String) {
        viewModelScope.launch {
            val children: Map<String, Child> = parentRepository.fetchChildren(parentId)
            _children.value = children.values.toList()
        }
    }
}
