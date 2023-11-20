package com.n1.moguchi.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.data.repositories.ParentRepository
import com.n1.moguchi.data.repositories.TaskRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class GoalCreationViewModel @Inject constructor(
    private val parentRepository: ParentRepository,
    private val goalRepository: GoalRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    private val _progressGoal = MutableLiveData<String>()
    val progress: LiveData<String> = _progressGoal

    private val _goalHeight = MutableLiveData<Int>()
    val goalHeight: LiveData<Int> = _goalHeight

    private val _taskHeight = MutableLiveData<Int>()
    val taskHeight: LiveData<Int> = _taskHeight

    private val _goalName = MutableLiveData<String>()
    val goalName: LiveData<String> = _goalName

    private var counterHeight = 1

    init {
        _goalHeight.value = counterHeight
    }

    fun getChildren(parentId: String) {
        viewModelScope.launch {
            val children: Map<String, Child> = parentRepository.getChildren(parentId)
            _children.value = children.values.toList()
        }
    }

    fun createGoal(goal: Goal, childId: String) {
        goalRepository.createGoal(goal, childId)
    }

    fun createTask() {

    }

    fun increaseHeight() {
        if (counterHeight < 10) {
            _goalHeight.value = ++counterHeight
        }
    }

    fun decreaseHeight() {
        if (counterHeight != 1) {
            _goalHeight.value = --counterHeight
        }
    }
}