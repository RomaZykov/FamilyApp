package com.n1.moguchi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.repositories.ParentRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val parentRepository: ParentRepository) : ViewModel() {

    private val _children = MutableLiveData<Map<String, Child>>()
    val children: LiveData<Map<String, Child>> = _children

    fun getChildrenList(parentId: String) {
        viewModelScope.launch {
            val children: Map<String, Child> = parentRepository.getChildren(parentId)
            _children.value = children
        }
    }

    fun getTasksListByChildId() {
        TODO()
    }
}