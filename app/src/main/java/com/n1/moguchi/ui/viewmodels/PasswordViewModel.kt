package com.n1.moguchi.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.repositories.ParentRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class PasswordViewModel @Inject constructor(private val parentRepository: ParentRepository) :
    ViewModel() {

    fun setPassword(password: Int, childID: String) {
        viewModelScope.launch {
            parentRepository.setPassword(password, childID)
        }
    }
}