package com.example.familyapp.ui.fragment.parent.task_creation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familyapp.data.remote.model.Child
import com.example.familyapp.data.remote.model.Goal
import com.example.familyapp.data.remote.model.Task
import com.example.familyapp.domain.repositories.GoalRepository
import com.example.familyapp.domain.repositories.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskCreationViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    private val _totalGoalPoints = MutableLiveData<Int>()
    val totalGoalPoints: LiveData<Int> = _totalGoalPoints

    private val _currentGoalPoints = MutableLiveData<Int>()
    val currentGoalPoints: LiveData<Int> = _currentGoalPoints

    private val tasksList = mutableListOf<Task>()
    private var tasksHeightTotal = 0


    init {
        _tasks.value = tasksList
        _currentGoalPoints.value = tasksHeightTotal
    }

    fun returnCreatedTask(goalId: String): Task {
        val preparedTask = taskRepository.createTask(goalId)
        tasksList.add(preparedTask)
        tasksHeightTotal += preparedTask.height
        _currentGoalPoints.value = tasksHeightTotal
        _tasks.value = tasksList
        return tasksList.last()
    }

    fun setupGoal(relatedGoal: Goal) {
        _totalGoalPoints.value = relatedGoal.totalPoints
    }

    fun setupMaxPointsOfGoal(goalId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _totalGoalPoints.postValue(goalRepository.getGoal(goalId).totalPoints)
        }
    }

    fun deleteTask(task: Task) {
        _tasks.value = _tasks.value?.dropWhile {
            it.taskId == task.taskId
        }
        tasksHeightTotal -= task.height
        _currentGoalPoints.value = tasksHeightTotal
    }

    fun onTaskUpdate(task: Task, positiveTaskPointsChange: Boolean) {
        val tasks = _tasks.value?.map {
            if (it.taskId == task.taskId) {
                it.copy(title = task.title, height = task.height)
            } else {
                it
            }
        }
        _tasks.value = tasks!!
        if (positiveTaskPointsChange) {
            ++tasksHeightTotal
            _currentGoalPoints.value = tasksHeightTotal
        } else {
            --tasksHeightTotal
            _currentGoalPoints.value = tasksHeightTotal
        }
    }
}
