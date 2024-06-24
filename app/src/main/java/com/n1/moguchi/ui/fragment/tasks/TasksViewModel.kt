package com.n1.moguchi.ui.fragment.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.local.UserPreferences
import com.n1.moguchi.data.remote.model.Task
import com.n1.moguchi.di.modules.IoDispatcher
import com.n1.moguchi.domain.repositories.AppRepository
import com.n1.moguchi.domain.repositories.GoalRepository
import com.n1.moguchi.domain.repositories.TaskRepository
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
                    _completedTasks.value = it
                }
        }
    }

    fun fetchActiveTasks(goalId: String) {
        viewModelScope.launch(dispatcher) {
            taskRepository.fetchActiveTasks(goalId)
                .collect {
                    _activeTasks.value = it
                }
        }
    }

    fun fetchCompletedTasks(goalId: String) {
        viewModelScope.launch(dispatcher) {
            taskRepository.fetchCompletedTasks(goalId)
                .collect {
                    _completedTasks.value = it
                }
        }
    }

    fun deleteTask(goalId: String, task: Task) {
        viewModelScope.launch(dispatcher) {
            taskRepository.deleteTask(goalId, task)
            if (task.taskCompleted) {
                _completedTasks.value =
                    _completedTasks.value?.dropWhile { it.taskId == task.taskId }
            } else {
                _activeTasks.value = _activeTasks.value?.dropWhile { it.taskId == task.taskId }
            }
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
        viewModelScope.launch(dispatcher) {
            if (task.taskCompleted) {
                val taskToUpdate = _completedTasks.value?.find { it.taskId == task.taskId }
                val changedTask = taskToUpdate?.copy(taskCompleted = false, onCheck = false)
                _completedTasks.value =
                    _completedTasks.value?.dropWhile { it.taskId == task.taskId }
                if (changedTask != null) {
                    _activeTasks.value = _activeTasks.value?.plus(changedTask)
                    val currentPoints = _currentGoalPoints.value
                    _currentGoalPoints.value =
                        if (currentPoints!! - task.height < 0) 0 else _currentGoalPoints.value?.minus(
                            task.height
                        )
                    taskRepository.updateTask(changedTask)
                }
            } else {
                val taskToUpdate = _activeTasks.value?.find { it.taskId == task.taskId }
                val changedTask = taskToUpdate?.copy(taskCompleted = true, onCheck = false)
                _activeTasks.value = _activeTasks.value?.dropWhile { it.taskId == task.taskId }
                if (changedTask != null) {
                    _completedTasks.value = _completedTasks.value?.plus(changedTask)
                    _currentGoalPoints.value = _currentGoalPoints.value?.plus(task.height)
                    taskRepository.updateTask(changedTask)
                }
            }
        }
    }

    fun updateTaskCheckStatus(task: Task) {
        viewModelScope.launch(dispatcher) {
            val updatedTask = task.copy(
                onCheck = !task.onCheck
            )
            if (task.taskCompleted) {
                _completedTasks.value =
                    _completedTasks.value?.dropWhile { it.taskId == task.taskId }
                _completedTasks.value = _completedTasks.value?.plus(updatedTask)
            } else {
                _activeTasks.value = _activeTasks.value?.dropWhile { it.taskId == task.taskId }
                _activeTasks.value = _activeTasks.value?.plus(updatedTask)
            }
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
