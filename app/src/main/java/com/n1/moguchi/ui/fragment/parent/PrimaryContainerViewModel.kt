package com.n1.moguchi.ui.fragment.parent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.remote.model.Child
import com.n1.moguchi.data.remote.model.Goal
import com.n1.moguchi.data.remote.model.Task
import com.n1.moguchi.domain.repositories.GoalRepository
import com.n1.moguchi.domain.repositories.ParentRepository
import com.n1.moguchi.domain.repositories.TaskRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class PrimaryContainerViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val taskRepository: TaskRepository,
    private val parentRepository: ParentRepository,
) : ViewModel() {

    fun saveChildrenToDb(children: List<Child>) {
        viewModelScope.launch {
            parentRepository.saveChildrenToDb(children, null, null)
        }
    }

    fun saveGoalWithTasksToDb(goal: Goal, tasks: List<Task>) {
        viewModelScope.launch {
            goalRepository.saveGoalWithTasksToDb(goal, tasks)
        }
    }

    fun saveTasksToDb(tasks: List<Task>) {
        val goalId = tasks.last().goalOwnerId
        viewModelScope.launch {
            taskRepository.saveTasksToDb(goalId!!, tasks)
        }
    }
}
