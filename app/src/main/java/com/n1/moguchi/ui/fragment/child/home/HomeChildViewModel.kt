package com.n1.moguchi.ui.fragment.child.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.ChildRepository
import com.n1.moguchi.data.repositories.GoalRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeChildViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val childRepository: ChildRepository
) : ViewModel() {

    private val _goals = MutableLiveData<Map<Goal, List<Task>>>()
    val goals: LiveData<Map<Goal, List<Task>>> = _goals

    private val _totalTasks = MutableLiveData<Int>()
    val totalTasks: LiveData<Int> = _totalTasks

    private val _completedGoals = MutableLiveData<List<Goal>>()
    val completedGoals: LiveData<List<Goal>> = _completedGoals

    private val _child = MutableLiveData<Child>()
    val child: LiveData<Child> = _child

    fun getChild(childId: String) {
        viewModelScope.launch {
            childRepository.getChild(childId).collect {
                _child.value = it
            }
        }
    }

    fun fetchGoalsAndTasks(childID: String) {
        viewModelScope.launch {
            val goals = goalRepository.fetchChildGoals(childID)
            val goalsWithTasksMap = goalRepository.fetchTasks(goals)
            _goals.value = goalsWithTasksMap
        }
    }

    fun fetchCompletedGoals(childID: String) {
        viewModelScope.launch {
            val completedGoals = goalRepository.fetchCompletedGoals(childID)
            _completedGoals.value = completedGoals
        }
    }
}