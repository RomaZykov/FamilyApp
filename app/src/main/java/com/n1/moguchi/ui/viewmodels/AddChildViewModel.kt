package com.n1.moguchi.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.n1.moguchi.data.repositories.ParentRepository
import javax.inject.Inject

class AddChildViewModel @Inject constructor(
    private val parentRepository: ParentRepository
) : ViewModel() {

    private var _childrenNames = MutableLiveData<List<String>>()
    val childrenNames: LiveData<List<String>> = _childrenNames

    private var _childName = MutableLiveData<String>()
    val childName: LiveData<String> = _childName

    private var _isAvatarSelected = MutableLiveData<Boolean>()
    val isAvatarSelected: LiveData<Boolean> = _isAvatarSelected

    fun saveChildren(parentId: String, childrenNames: List<String>) {
        parentRepository.saveChildrenByName(parentId, childrenNames)
    }
}