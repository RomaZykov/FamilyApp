package com.example.familyapp.ui.fragment.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familyapp.data.local.UserPreferences
import com.example.familyapp.data.remote.model.Task
import com.example.familyapp.di.modules.IoDispatcher
import com.example.familyapp.domain.repositories.AppRepository
import com.example.familyapp.domain.repositories.GoalRepository
import com.example.familyapp.domain.repositories.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val taskRepository: TaskRepository,
    private val goalRepository: GoalRepository,
    private val appRepository: AppRepository
) : ViewModel() {

    private val _activeTasks = MutableLiveData<List<Task>>()
    val activeTasks: LiveData<List<Task>> = _activeTasks

    private val _completedTasks = MutableLiveData<List<Task>>()
    val completedTasks: LiveData<List<Task>> = _completedTasks

    private val _totalGoalPoints = MutableLiveData<Int>()
    val totalGoalPoints: LiveData<Int> = _totalGoalPoints

    private val _currentGoalPoints = MutableLiveData<Int>()
    val currentGoalPoints: LiveData<Int> = _currentGoalPoints

    private val _goalTitle = MutableLiveData<String>()
    val goalTitle: LiveData<String> = _goalTitle

    fun getUserPrefs(): Flow<UserPreferences> {
        return appRepository.getUserPrefs().map {
            it
        }
    }

    fun fetchAllTasks(goalId: String) {
        viewModelScope.launch(dispatcher) {
            taskRepository.fetchAllTasks(goalId)
                .collect {
                    _completedTasks.postValue(it)
                }
        }
    }

    fun fetchActiveTasks(goalId: String) {
        viewModelScope.launch(dispatcher) {
            taskRepository.fetchActiveTasks(goalId)
                .collect {
                    _activeTasks.postValue(it)
                }
        }
    }

    fun fetchCompletedTasks(goalId: String) {
        viewModelScope.launch(dispatcher) {
            taskRepository.fetchCompletedTasks(goalId)
                .collect {
                    _completedTasks.postValue(it)
                }
        }
    }

    fun deleteTask(goalId: String, task: Task) {
        if (task.taskCompleted) {
            _completedTasks.value =
                _completedTasks.value?.dropWhile { it.taskId == task.taskId }
        } else {
            _activeTasks.value = _activeTasks.value?.dropWhile { it.taskId == task.taskId }
        }
        viewModelScope.launch(dispatcher) {
            taskRepository.deleteTask(goalId, task)
        }
    }

    fun setupRelatedGoalDetails(goalId: String) {
        viewModelScope.launch(dispatcher) {
            goalRepository.getGoal(goalId).also {
                _currentGoalPoints.postValue(it.currentPoints)
                _totalGoalPoints.postValue(it.totalPoints)
                _goalTitle.postValue(it.title)
            }
        }
    }

    fun updateTaskStatus(task: Task) {
        if (task.taskCompleted) {
            val taskToUpdate = _completedTasks.value?.find { it.taskId == task.taskId }
            val changedTask = taskToUpdate?.copy(taskCompleted = false, onCheck = false)
            _completedTasks.value = _completedTasks.value?.dropWhile { it.taskId == task.taskId }
            if (changedTask != null) {
                _activeTasks.value = _activeTasks.value?.plus(changedTask)
                val currentPoints = _currentGoalPoints.value
                _currentGoalPoints.value =
                    if (currentPoints!! - task.height < 0) 0 else _currentGoalPoints.value?.minus(
                        task.height
                    )
                viewModelScope.launch(dispatcher) {
                    taskRepository.updateTask(changedTask)
                }
            }
        } else {
            val taskToUpdate = _activeTasks.value?.find { it.taskId == task.taskId }
            val changedTask = taskToUpdate?.copy(taskCompleted = true, onCheck = false)
            _activeTasks.value = _activeTasks.value?.dropWhile { it.taskId == task.taskId }
            if (changedTask != null) {
                _completedTasks.value = _completedTasks.value?.plus(changedTask)
                _currentGoalPoints.value = _currentGoalPoints.value?.plus(task.height)
                viewModelScope.launch(dispatcher) {
                    taskRepository.updateTask(changedTask)
                }
            }
        }
    }

    fun updateTaskCheckStatus(task: Task) {
        val updatedTask = task.copy(onCheck = !task.onCheck)
        if (task.taskCompleted) {
            _completedTasks.value = _completedTasks.value?.dropWhile { it.taskId == task.taskId }
            _completedTasks.value = _completedTasks.value?.plus(updatedTask)
        } else {
            _activeTasks.value = _activeTasks.value?.dropWhile { it.taskId == task.taskId }
            _activeTasks.value = _activeTasks.value?.plus(updatedTask)
        }
        viewModelScope.launch(dispatcher) {
            taskRepository.updateTask(updatedTask)
        }
    }

    fun updateRelatedGoal(goalId: String, taskHeight: Int, isActiveTask: Boolean?) {
        viewModelScope.launch(dispatcher) {
            if (isActiveTask != null) {
                if (isActiveTask == true) {
                    goalRepository.updateGoalPoints(goalId, taskHeight)
                } else {
                    goalRepository.updateGoalPoints(goalId, -taskHeight)
                }
            }

            goalRepository.getGoal(goalId).also {
                if (it.currentPoints >= it.totalPoints && !it.goalCompleted) {
                    goalRepository.updateGoalStatus(goalId)
                }
                if (it.currentPoints < it.totalPoints && it.goalCompleted) {
                    goalRepository.updateGoalStatus(goalId)
                }
            }
        }
    }
}
