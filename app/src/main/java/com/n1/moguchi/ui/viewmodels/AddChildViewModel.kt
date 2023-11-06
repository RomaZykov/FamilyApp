package com.n1.moguchi.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.repositories.ParentRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddChildViewModel @Inject constructor(
    private val parentRepository: ParentRepository
) : ViewModel() {

    private var _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    fun getChildren(parentId: String) {
        viewModelScope.launch {
            val children = parentRepository.getChildren(parentId)
            _children.value = children.map {
                it.value
            }
        }
    }

    fun deleteChildProfile(parentId: String, childId: String) {
        _children.value = _children.value?.apply {
            this.find { it.childId == childId && it.parentOwnerId == parentId }
        }
        parentRepository.deleteChildProfile()
    }

    fun createNewChild(parentId: String, child: Child) {
        _children.value = (_children.value ?: emptyList()) + child
        parentRepository.saveChild(parentId, child)
    }
}