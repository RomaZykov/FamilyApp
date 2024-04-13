package com.n1.moguchi.ui.fragment.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.local.UserPreferences
import com.n1.moguchi.data.models.remote.Task
import com.n1.moguchi.data.repositories.AppRepository
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.data.repositories.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksViewModel @Inject constructor(
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
        viewModelScope.launch {
            taskRepository.fetchAllTasks(goalId)
                .collect {
                    _completedTasks.value = it
                }
        }
    }

    fun fetchActiveTasks(goalId: String) {
        viewModelScope.launch {
            taskRepository.fetchActiveTasks(goalId)
                .collect {
                    _activeTasks.value = it
                }
        }
    }

    fun fetchCompletedTasks(goalId: String) {
        viewModelScope.launch {
            taskRepository.fetchCompletedTasks(goalId)
                .collect {
                    _completedTasks.value = it
                }
        }
    }

    fun deleteTask(goalId: String, task: Task, isActiveTask: Boolean) {
        viewModelScope.launch {
            taskRepository.deleteTask(goalId, task)
            if (isActiveTask) {
                _activeTasks.value = _activeTasks.value?.dropWhile { it.taskId == task.taskId }
            } else {
                _completedTasks.value =
                    _completedTasks.value?.dropWhile { it.taskId == task.taskId }
            }
        }
    }

    fun setupRelatedGoalDetails(goalId: String) {
        viewModelScope.launch {
            goalRepository.getGoal(goalId).also {
                _currentGoalPoints.value = it.currentPoints
                _totalGoalPoints.value = it.totalPoints
                _goalTitle.value = it.title
            }
        }
    }

    fun updateTaskStatus(task: Task, isActiveTask: Boolean) {
        viewModelScope.launch {
            if (isActiveTask) {
                val taskToUpdate = _activeTasks.value?.single { it.taskId == task.taskId }.also {
                    it?.taskCompleted = true
                }
                _activeTasks.value = _activeTasks.value?.dropWhile { it.taskId == task.taskId }
                if (taskToUpdate != null) {
                    _completedTasks.value = _completedTasks.value?.plus(taskToUpdate)
                }
                updateGoalProgression(task)
            } else {
                val taskToUpdate = _completedTasks.value?.single { it.taskId == task.taskId }.also {
                    it?.taskCompleted = false
                }
                _completedTasks.value =
                    _completedTasks.value?.dropWhile { it.taskId == task.taskId }
                if (taskToUpdate != null) {
                    _activeTasks.value = _activeTasks.value?.plus(taskToUpdate)
                }
                updateGoalProgression(task)
            }
            taskRepository.updateTask(task)
        }
    }

    fun updateRelatedGoal(goalId: String, taskHeight: Int, isActiveTask: Boolean) {
        viewModelScope.launch {
            if (isActiveTask) {
                goalRepository.updateGoalPoints(goalId, taskHeight)
            } else {
                goalRepository.updateGoalPoints(goalId, -taskHeight)
            }
            goalRepository.getGoal(goalId).also {
                if (it.currentPoints >= it.totalPoints) {
                    goalRepository.updateGoalStatus(goalId)
                }
            }
        }
    }

    private fun updateGoalProgression(task: Task) {
        viewModelScope.launch {
            goalRepository.getGoal(task.goalOwnerId!!).also {
                if (task.taskCompleted) {
                    _currentGoalPoints.value = _currentGoalPoints.value?.plus(task.height)
                } else {
                    _currentGoalPoints.value = _currentGoalPoints.value?.minus(task.height)
                }
            }
        }
    }
}
