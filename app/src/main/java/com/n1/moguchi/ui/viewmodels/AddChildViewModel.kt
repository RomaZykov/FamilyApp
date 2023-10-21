package com.n1.moguchi.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.repositories.ParentRepository
import javax.inject.Inject

class AddChildViewModel @Inject constructor(
    private val parentRepository: ParentRepository
) : ViewModel() {

    private var _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    fun createNewChild(parentId: String, child: Child) {
        _children.value = _children.value?.plus(child)
        parentRepository.saveChild(parentId, child)
    }
}