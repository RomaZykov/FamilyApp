package com.n1.moguchi.presentation.fragment.parent.task_creation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.remote.model.Child
import com.n1.moguchi.data.remote.model.Goal
import com.n1.moguchi.data.remote.model.Task
import com.n1.moguchi.domain.repositories.GoalRepository
import com.n1.moguchi.domain.repositories.TaskRepository
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
    private var taskHeightTotal = 0

    init {
        _tasks.value = tasksList
        _currentGoalPoints.value = taskHeightTotal
    }

    fun returnCreatedTask(goalId: String): Task {
        val preparedTask = taskRepository.createTask(goalId)
        tasksList.add(preparedTask)
        taskHeightTotal += preparedTask.height
        _currentGoalPoints.value = taskHeightTotal
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
        taskHeightTotal -= task.height
        _currentGoalPoints.value = taskHeightTotal
    }

    fun onTaskUpdate(task: Task, taskPointsChanged: Boolean) {
        _tasks.value?.find {
            it.taskId == task.taskId
        }.also {
            it?.copy(title = task.title, height = task.height)
        }
        if (taskPointsChanged) {
            ++taskHeightTotal
            _currentGoalPoints.value = taskHeightTotal
        } else {
            --taskHeightTotal
            _currentGoalPoints.value = taskHeightTotal
        }
    }
}
