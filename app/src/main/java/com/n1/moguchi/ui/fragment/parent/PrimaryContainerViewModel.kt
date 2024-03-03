package com.n1.moguchi.ui.fragment.parent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.GoalRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class PrimaryContainerViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
) : ViewModel() {

    fun saveGoalWithTasksToDb(goal: Goal, tasks: List<Task>) {
        viewModelScope.launch {
            goalRepository.saveGoalWithTasksToDb(goal, tasks)
        }
    }
}
