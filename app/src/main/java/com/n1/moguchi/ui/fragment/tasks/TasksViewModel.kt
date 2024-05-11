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

    private val _secondaryProgression = MutableLiveData<Int>()
    val secondaryProgression: LiveData<Int> = _secondaryProgression

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
                _activeTasks.value?.dropWhile { it.taskId == task.taskId }
            } else {
                _completedTasks.value?.dropWhile { it.taskId == task.taskId }
            }
        }
    }

    fun setupRelatedGoalDetails(goalId: String) {
        viewModelScope.launch {
            goalRepository.getGoal(goalId).also {
                _currentGoalPoints.value = it.currentPoints
                _totalGoalPoints.value = it.totalPoints
                _secondaryProgression.value = it.secondaryPoints + it.currentPoints
                _goalTitle.value = it.title
            }
        }
    }

    fun updateTaskStatus(task: Task, isActiveTask: Boolean) {
        viewModelScope.launch {
            if (isActiveTask) {
                val taskToUpdate = _activeTasks.value?.find { it.taskId == task.taskId }.also {
                    it?.taskCompleted = true
                }
                _activeTasks.value?.dropWhile { it.taskId == task.taskId }
                if (taskToUpdate != null) {
                    _completedTasks.value?.plus(taskToUpdate)
                    taskRepository.updateTask(taskToUpdate)
                }
            } else {
                val taskToUpdate = _completedTasks.value?.find { it.taskId == task.taskId }.also {
                    it?.taskCompleted = false
                }
                _completedTasks.value?.dropWhile { it.taskId == task.taskId }
                if (taskToUpdate != null) {
                    _activeTasks.value?.plus(taskToUpdate)
                    taskRepository.updateTask(taskToUpdate)
                }
            }

            // Update goal progression
            goalRepository.getGoal(task.goalOwnerId!!).also {
                if (task.taskCompleted) {
                    _currentGoalPoints.value?.plus(task.height)
                } else {
                    _currentGoalPoints.value?.minus(task.height)
                }
            }
        }
    }

    fun updateTaskToCheckStatus(task: Task) {
        viewModelScope.launch {
            goalRepository.getGoal(task.goalOwnerId!!).also {
                _secondaryProgression.value?.plus(task.height)
            }
            val updatedTask = task.copy(
                onCheck = !task.onCheck
            )
            taskRepository.updateTask(updatedTask)
        }
    }

    fun updateRelatedGoal(goalId: String, taskHeight: Int, isActiveTask: Boolean?) {
        viewModelScope.launch {
            if (isActiveTask != null) {
                if (isActiveTask == true) {
                    goalRepository.updateGoalPoints(goalId, taskHeight)
                } else {
                    goalRepository.updateGoalPoints(goalId, -taskHeight)
                }
            } else {
                goalRepository.updateSecondaryGoalPoints(goalId, taskHeight)
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
