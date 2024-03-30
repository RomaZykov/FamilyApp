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
import kotlinx.coroutines.Dispatchers
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

    private var activeTasksList = mutableListOf<Task>()
    private var completedTasksList = mutableListOf<Task>()

    fun getUserPrefs(): Flow<UserPreferences> {
        return appRepository.getUserPrefs().map {
            it
        }
    }

    fun updateRelatedGoal(goalId: String, taskHeight: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            goalRepository.updateGoal(goalId, taskHeight)
        }
    }

    fun fetchActiveTasks(goalId: String) {
        viewModelScope.launch {
            activeTasksList = taskRepository.fetchActiveTasks(goalId).toMutableList()
            _activeTasks.value = activeTasksList
        }
    }

    fun fetchCompletedTasks(goalId: String) {
        viewModelScope.launch {
            completedTasksList = taskRepository.fetchCompletedTasks(goalId).toMutableList()
            _completedTasks.value = completedTasksList
        }
    }

    fun deleteTask(goalId: String, task: Task, isActiveTask: Boolean) {
        viewModelScope.launch {
            if (isActiveTask) {
                _activeTasks.value = activeTasksList.dropWhile {
                    it.taskId == task.taskId
                }
            } else {
                _completedTasks.value = completedTasksList.dropWhile {
                    it.taskId == task.taskId
                }
            }
            taskRepository.deleteTask(goalId, task)
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
                val taskToUpdate = activeTasksList.single { it.taskId == task.taskId }.also {
                    it.taskCompleted = true
                }
                _activeTasks.value = activeTasksList.dropWhile { it.taskId == task.taskId }

                completedTasksList.add(taskToUpdate)
                _completedTasks.value = completedTasksList
            } else {
                val taskToUpdate = completedTasksList.single { it.taskId == task.taskId }.also {
                    it.taskCompleted = false
                }
                _completedTasks.value = completedTasksList.dropWhile { it.taskId == task.taskId }

                activeTasksList.add(taskToUpdate)
                _activeTasks.value = activeTasksList
            }
            updateProgression(task)
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
