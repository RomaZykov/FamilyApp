package com.n1.moguchi.ui.viewmodels

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

private const val MAX_GOAL_HEIGHT = 10
private const val MIN_GOAL_HEIGHT = 1

class GoalCreationViewModel @Inject constructor(
    private val parentRepository: ParentRepository,
    private val goalRepository: GoalRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    private val _goalID = MutableLiveData<String>()
    val goalID: LiveData<String> = _goalID

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

//    private val _progressGoalHeight = MutableLiveData<String>()
//    val progressGoalHeight: LiveData<String> = _progressGoalHeight

    private val _goalHeight = MutableLiveData<Int>()
    val goalHeight: LiveData<Int> = _goalHeight

    private val _goalName = MutableLiveData<String>()
    val goalName: LiveData<String> = _goalName

    private var counterGoalHeight = MIN_GOAL_HEIGHT

    init {
        _goalHeight.value = counterGoalHeight
    }

    fun getChildren(parentId: String) {
        viewModelScope.launch {
            val children: Map<String, Child> = parentRepository.getChildren(parentId)
            _children.value = children.values.toList()
        }
    }

//    fun getTasksByGoalId(goalId: String) {
//        viewModelScope.launch {
//            val tasks: List<Task> = taskRepository.getTasks(goalId)
//            _tasks.value = tasks.toList()
//        }
//    }

    fun createGoal(goal: Goal, childId: String) {
        goalRepository.createGoal(goal, childId).also {
            _goalID.value = it.goalId!!
        }
    }

    fun increaseGoalHeight() {
        if (counterGoalHeight < MAX_GOAL_HEIGHT) {
            _goalHeight.value = ++counterGoalHeight
        }
    }

    fun decreaseGoalHeight() {
        if (counterGoalHeight != MIN_GOAL_HEIGHT) {
            _goalHeight.value = --counterGoalHeight
        }
    }

    fun setGoalTitle(title: String) {
        _goalName.value = title
    }
}