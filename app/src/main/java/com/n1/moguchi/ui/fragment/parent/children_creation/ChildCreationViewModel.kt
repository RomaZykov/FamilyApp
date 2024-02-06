package com.n1.moguchi.ui.fragment.parent.children_creation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.repositories.ParentRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChildCreationViewModel @Inject constructor(private val parentRepository: ParentRepository) :
    ViewModel() {

    private var _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    private val childrenList = mutableListOf<Child>()

    fun fetchChildren(parentId: String) {
        viewModelScope.launch {
            val children: Map<String, Child> = parentRepository.fetchChildren(parentId)
            childrenList.addAll(children.values)
            _children.value = childrenList
        }
    }

    fun createNewChild(parentId: String, child: Child): Child {
        viewModelScope.launch {
            val newChild = parentRepository.getAndSaveChildToDb(parentId, child)
            childrenList.add(newChild)
            _children.value = childrenList
        }
        return childrenList.last()
    }

    fun deleteChildProfile(parentId: String, childId: String) {
        _children.value =
            _children.value?.dropWhile { it.childId == childId && it.parentOwnerId == parentId }
        viewModelScope.launch {
            parentRepository.deleteChildProfile(parentId, childId)
        }
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