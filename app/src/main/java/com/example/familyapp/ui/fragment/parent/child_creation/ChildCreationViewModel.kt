package com.example.familyapp.ui.fragment.parent.child_creation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familyapp.data.remote.model.Child
import com.example.familyapp.domain.repositories.ParentRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChildCreationViewModel @Inject constructor(
    private val parentRepository: ParentRepository
) : ViewModel() {

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

    fun saveChildrenData(children: List<Child>) {
        viewModelScope.launch {
            parentRepository.saveChildrenToDb(children, null, null)
        }
    }
}