package com.n1.moguchi.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.repositories.ParentRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val parentRepository: ParentRepository
) : ViewModel() {

    private val _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    fun getChildren(parentId: String) {
        viewModelScope.launch {
            val children: Map<String, Child> = parentRepository.fetchChildren(parentId)
            _children.value = children.values.toList()
        }
    }
}
