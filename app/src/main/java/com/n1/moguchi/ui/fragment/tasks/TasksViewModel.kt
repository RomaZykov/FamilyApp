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

    private val _currentAndTotalGoalPoints = MutableLiveData<Map<Int, Int>>()
    val currentAndTotalGoalPoints: LiveData<Map<Int, Int>> = _currentAndTotalGoalPoints

    private val _goalTitle = MutableLiveData<String>()
    val goalTitle: LiveData<String> = _goalTitle

    fun getUserPrefs(): Flow<UserPreferences> {
        return appRepository.getUserPrefs().map {
            it
        }
    }

    fun fetchActiveTasks(goalId: String) {
        viewModelScope.launch {
            val activeTasks = taskRepository.fetchActiveTasks(goalId)
            _activeTasks.value = activeTasks
        }
    }

    fun fetchCompletedTasks(goalId: String) {
        viewModelScope.launch {
            val completedTasks = taskRepository.fetchCompletedTasks(goalId)
            _completedTasks.value = completedTasks
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
                val currentAndTotalGoalPointsMap: MutableMap<Int, Int> = mutableMapOf()
                currentAndTotalGoalPointsMap[it.currentPoints] = it.totalPoints
                _currentAndTotalGoalPoints.value = currentAndTotalGoalPointsMap
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
                    _completedTasks.value =
                        _completedTasks.value?.plus(taskToUpdate)
                }
                updateProgression(task)
            } else {
                val taskToUpdate = _completedTasks.value?.single { it.taskId == task.taskId }.also {
                    it?.taskCompleted = false
                }
                _completedTasks.value =
                    _completedTasks.value?.dropWhile { it.taskId == task.taskId }
                if (taskToUpdate != null) {
                    _activeTasks.value =
                        _activeTasks.value?.plus(taskToUpdate)
                }
                updateProgression(task)
            }
            taskRepository.updateTask(task)
        }
    }

    private fun updateProgression(task: Task) {
        viewModelScope.launch {
            goalRepository.getGoal(task.goalOwnerId!!).also {
                val currentAndTotalGoalPointsMap: MutableMap<Int, Int> = mutableMapOf()
                currentAndTotalGoalPointsMap[it.currentPoints + task.height] = it.totalPoints
                _currentAndTotalGoalPoints.value = currentAndTotalGoalPointsMap
            }
        }
    }
}
