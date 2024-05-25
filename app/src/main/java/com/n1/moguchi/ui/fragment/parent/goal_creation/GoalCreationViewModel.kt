package com.n1.moguchi.ui.fragment.parent.goal_creation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.remote.model.Child
import com.n1.moguchi.data.remote.model.Goal
import com.n1.moguchi.data.remote.model.Task
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.data.repositories.ParentRepository
import com.n1.moguchi.data.repositories.TaskRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MAX_GOAL_HEIGHT = 10
private const val MIN_GOAL_HEIGHT = 1

open class GoalCreationViewModel @Inject constructor(
    private val parentRepository: ParentRepository,
    private val goalRepository: GoalRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    private val _goal = MutableLiveData<Goal>()
    val goal: LiveData<Goal> = _goal

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

//    private val _progressGoalHeight = MutableLiveData<String>()
//    val progressGoalHeight: LiveData<String> = _progressGoalHeight

    private val _totalGoalPoints = MutableLiveData<Int>()
    val totalGoalPoints: LiveData<Int> = _totalGoalPoints

    private val _goalName = MutableLiveData<String>()
    val goalName: LiveData<String> = _goalName

    private var counterGoalHeight = MIN_GOAL_HEIGHT

    init {
        _totalGoalPoints.value = counterGoalHeight
    }

    fun createGoal(title: String, totalPoints: Int, childId: String) {
        _goal.value = goalRepository.returnCreatedGoal(title, totalPoints, childId)
    }

    fun increaseGoalHeight() {
        if (counterGoalHeight < MAX_GOAL_HEIGHT) {
            _totalGoalPoints.value = ++counterGoalHeight
        }
    }

    fun decreaseGoalHeight() {
        if (counterGoalHeight != MIN_GOAL_HEIGHT) {
            _totalGoalPoints.value = --counterGoalHeight
        }
    }

    fun setGoalTitle(title: String) {
        _goalName.value = title
    }

    fun setChildrenFromBundle(children: List<Child>) {
        _children.value = children
    }

    fun getChildren(parentId: String) {
        viewModelScope.launch {
            val children: Map<String, Child> = parentRepository.fetchChildren(parentId)
            _children.value = children.values.toList()
        }
    }
}