package com.n1.moguchi.ui.fragment.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.data.repositories.TaskRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val goalRepository: GoalRepository
) : ViewModel() {

    private val _activeTasks = MutableLiveData<List<Task>>()
    val activeTasks: LiveData<List<Task>> = _activeTasks

    private val _completedTasks = MutableLiveData<List<Task>>()
    val completedTasks: LiveData<List<Task>> = _completedTasks

    private val _currentAndTotalGoalPoints = MutableLiveData<Map<Int, Int>>()
    val currentAndTotalGoalPoints: LiveData<Map<Int, Int>> = _currentAndTotalGoalPoints

    private val currentGoalPoints = _currentAndTotalGoalPoints.value?.keys
    private val totalGoalPoints = _currentAndTotalGoalPoints.value?.values

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
                _completedTasks.value = _completedTasks.value?.dropWhile { it.taskId == task.taskId }
            }
        }
    }

    fun setCurrentProgression(goalId: String) {
        viewModelScope.launch {
            goalRepository.getGoal(goalId).also {
                val currentAndTotalGoalPointsMap: MutableMap<Int, Int> = mutableMapOf()
                currentAndTotalGoalPointsMap[it.currentPoints] = it.totalPoints
                _currentAndTotalGoalPoints.value = currentAndTotalGoalPointsMap
            }
        }
    }
}
