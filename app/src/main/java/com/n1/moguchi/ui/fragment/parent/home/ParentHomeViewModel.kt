package com.n1.moguchi.ui.fragment.parent.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.data.repositories.ParentRepository
import com.n1.moguchi.data.repositories.TaskRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ParentHomeViewModel @Inject constructor(
    private val parentRepository: ParentRepository,
    private val goalRepository: GoalRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _children = MutableLiveData<Map<String, Child>>()
    val children: LiveData<Map<String, Child>> = _children

    private val _goalsWithTasks = MutableLiveData<Map<Goal, List<Task>>>()
    val goalsWithTasks: LiveData<Map<Goal, List<Task>>> = _goalsWithTasks

    private val _totalTasks = MutableLiveData<Int>()
    val totalTasks: LiveData<Int> = _totalTasks

    fun getChildren(parentId: String) {
        viewModelScope.launch {
            val children: Map<String, Child> = parentRepository.getChildren(parentId)
            _children.value = children
        }
    }

    fun fetchGoalsAndTasks(childID: String) {
        viewModelScope.launch {
            val goals = goalRepository.getChildGoals(childID)
            val goalsWithTasksMap = goalRepository.fetchTasks(goals)
            _goalsWithTasks.value = goalsWithTasksMap
        }
    }

//    fun getTasksByGoalID(goalID: String) {
//        viewModelScope.launch {
//            val tasks = taskRepository.getTasks(goalID)
//            _goalsWithTasks.value = tasks
//        }
//    }
}