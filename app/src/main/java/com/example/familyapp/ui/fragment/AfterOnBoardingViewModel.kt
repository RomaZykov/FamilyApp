package com.example.familyapp.ui.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familyapp.data.remote.model.Child
import com.example.familyapp.data.remote.model.Goal
import com.example.familyapp.data.ProfileMode
import com.example.familyapp.data.remote.model.Task
import com.example.familyapp.domain.repositories.AppRepository
import com.example.familyapp.domain.repositories.ParentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AfterOnBoardingViewModel @Inject constructor(
    private val parentRepository: ParentRepository,
    private val appRepository: AppRepository
) : ViewModel() {

    fun saveChildrenDataToDb(children: List<Child>, goals: List<Goal>, tasks: List<Task>) {
        viewModelScope.launch {
            parentRepository.saveChildrenToDb(children, goals, tasks)
        }
    }

    fun updateUserPrefs(newMode: ProfileMode) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.updateUserPrefs(newMode, null)
        }
    }
}
