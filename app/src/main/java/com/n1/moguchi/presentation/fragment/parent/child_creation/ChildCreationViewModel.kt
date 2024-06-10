package com.n1.moguchi.presentation.fragment.parent.child_creation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.remote.model.Child
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

    fun returnCreatedChild(parentId: String, child: Child): Child {
        val preparedChild = parentRepository.returnCreatedChild(parentId, child)
        childrenList.add(preparedChild)
        _children.value = childrenList
        return childrenList.last()
    }

    fun deleteChildProfile(childId: String) {
        viewModelScope.launch {
            parentRepository.deleteChildProfile(childId)
        }
        childrenList.removeIf {
            it.childId == childId
        }
        _children.value = childrenList
    }

    fun onChildUpdate(child: Child) {
        _children.value?.find {
            it.childId == child.childId
        }.also {
            it?.copy(childName = child.childName, imageResourceId = child.imageResourceId)
        }
    }
}