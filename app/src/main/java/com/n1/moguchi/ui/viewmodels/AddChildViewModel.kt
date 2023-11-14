package com.n1.moguchi.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.repositories.ParentRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddChildViewModel @Inject constructor(private val parentRepository: ParentRepository) :
    ViewModel() {

    private var _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    init {
        _children.value = emptyList()
    }

    fun deleteChildProfile(parentId: String, childId: String) {
        _children.value =
            _children.value?.dropWhile { it.childId == childId && it.parentOwnerId == parentId }
        viewModelScope.launch {
            parentRepository.deleteChildProfile(parentId, childId)
        }
    }

    fun createNewChild(parentId: String, child: Child): Child {
        val newChild = parentRepository.getAndSaveChildToDb(parentId, child)
        _children.value = (_children.value ?: emptyList()) + newChild
        return newChild
    }

    fun onChildUpdate(parentId: String, child: Child): Child {
        val updatedChild = parentRepository.getAndUpdateChildInDb(parentId, child)
        _children.value?.find {
            it.childId == child.childId
        }.also {
            it?.childName = child.childName
            it?.imageResourceId = child.imageResourceId
        }
        return updatedChild
    }
}