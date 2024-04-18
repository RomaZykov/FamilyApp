package com.n1.moguchi.ui.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.remote.Child
import com.n1.moguchi.data.models.remote.Goal
import com.n1.moguchi.data.models.remote.ProfileMode
import com.n1.moguchi.data.models.remote.Task
import com.n1.moguchi.data.repositories.AppRepository
import com.n1.moguchi.data.repositories.ParentRepository
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
