package com.n1.moguchi.ui.fragment.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.local.UserPreferences
import com.n1.moguchi.data.remote.model.Task
import com.n1.moguchi.data.repositories.AppRepository
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.data.repositories.TaskRepository
import com.n1.moguchi.di.modules.IoDispatcher
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

    fun deleteTask(goalId: String, task: Task, isActiveTask: Boolean) {
        viewModelScope.launch(dispatcher) {
            taskRepository.deleteTask(goalId, task)
            if (isActiveTask) {
                _activeTasks.value = _activeTasks.value?.dropWhile { it.taskId == task.taskId }
            } else {
                _completedTasks.value = _completedTasks.value?.dropWhile { it.taskId == task.taskId }
            }
        }
    }

    fun setupRelatedGoalDetails(goalId: String) {
        viewModelScope.launch(dispatcher) {
            goalRepository.getGoal(goalId).also {
                _currentGoalPoints.value = it.currentPoints
                _totalGoalPoints.value = it.totalPoints
                _goalTitle.value = it.title
            }
        }
    }

    fun updateTaskStatus(task: Task, isActiveTask: Boolean) {
        viewModelScope.launch(dispatcher) {
            if (isActiveTask) {
                val taskToUpdate = _activeTasks.value?.find { it.taskId == task.taskId }.also {
                    it?.taskCompleted = true
                    it?.onCheck = false
                }
                _activeTasks.value = _activeTasks.value?.dropWhile { it.taskId == task.taskId }
                if (taskToUpdate != null) {
                    _completedTasks.value = _completedTasks.value?.plus(taskToUpdate)
                    _currentGoalPoints.value = _currentGoalPoints.value?.plus(task.height)
                    taskRepository.updateTask(taskToUpdate)
                }
            } else {
                val taskToUpdate = _completedTasks.value?.find { it.taskId == task.taskId }.also {
                    it?.taskCompleted = false
                    it?.onCheck = false
                }
                _completedTasks.value = _completedTasks.value?.dropWhile { it.taskId == task.taskId }
                if (taskToUpdate != null) {
                    _activeTasks.value = _activeTasks.value?.plus(taskToUpdate)
                    taskRepository.updateTask(taskToUpdate).also {
                        val currentPoints = _currentGoalPoints.value
                        _currentGoalPoints.value =
                            if (currentPoints!! - task.height < 0) 0 else _currentGoalPoints.value?.minus(
                                task.height
                            )
                    }
                }
            }
        }
    }

    fun updateTaskCheckStatus(task: Task) {
        viewModelScope.launch(dispatcher) {
            val updatedTask = task.copy(
                onCheck = !task.onCheck
            )
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
