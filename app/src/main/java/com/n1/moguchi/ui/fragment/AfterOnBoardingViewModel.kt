package com.n1.moguchi.ui.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.ParentRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AfterOnBoardingViewModel @Inject constructor(
    private val parentRepository: ParentRepository,
) : ViewModel() {

    fun saveChildrenDataToDb(children: List<Child>, goals: List<Goal>, tasks: List<Task>) {
        viewModelScope.launch {
            parentRepository.saveChildrenToDb(children, goals, tasks)
        }
    }
}
