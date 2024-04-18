package com.n1.moguchi.ui.fragment.switch_to_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.ProfileMode
import com.n1.moguchi.data.repositories.AppSettingsRepository
import com.n1.moguchi.data.repositories.ParentRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SwitchToUserViewModel @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
    private val parentRepository: ParentRepository
) : ViewModel() {

    private val _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    fun getProfileMode(): ProfileMode {
        return appSettingsRepository.getProfileMode()
    }

    fun getChildren(parentId: String) {
        viewModelScope.launch {
            val children: Map<String, Child> = parentRepository.fetchChildren(parentId)
            _children.value = children.values.toList()
        }
    }
}
