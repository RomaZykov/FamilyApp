package com.n1.moguchi.ui.fragment.parent.task_creation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.data.repositories.ParentRepository
import com.n1.moguchi.data.repositories.TaskRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private const val MIN_TASK_HEIGHT = 1
private const val MAX_TASK_HEIGHT = 3
private const val MIN_GOAL_HEIGHT = 0

class TaskCreationViewModel @Inject constructor(
    private val parentRepository: ParentRepository,
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

    private val _taskHeightTotal = MutableLiveData<Int>()
    val taskHeightTotal: LiveData<Int> = _taskHeightTotal

    private val _taskName = MutableLiveData<String>()
    val taskName: LiveData<String> = _taskName

    private var counterTaskHeight = MIN_TASK_HEIGHT

    init {
        _taskHeightTotal.value = counterTaskHeight
        _currentGoalPoints.value = MIN_GOAL_HEIGHT
    }

    fun createTask(task: Task, goalId: String): Task {
        val newTask = runBlocking {
            taskRepository.createTask(task, goalId)
        }
        _taskHeightTotal.value?.plus(newTask.height)
        _currentGoalPoints.value = _taskHeightTotal.value
        _tasks.value = (_tasks.value ?: emptyList()) + newTask
        return newTask
    }

    fun setupMaxPointsOfGoal(goalId: String) {
        viewModelScope.launch {
            _totalGoalPoints.postValue(goalRepository.getGoal(goalId).totalPoints)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
            _tasks.value =
                _tasks.value?.dropWhile { it.taskId == task.taskId }
            _taskHeightTotal.value?.minus(task.height)
            _currentGoalPoints.value = _taskHeightTotal.value
        }
    }

    fun getTasksByGoalId(goalId: String) {
        viewModelScope.launch {
            val tasks: List<Task> = taskRepository.getTasks(goalId)
            _tasks.value = (_tasks.value ?: emptyList()) + tasks
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }

    fun increaseTaskHeight() {
        _taskHeightTotal.value = ++counterTaskHeight
        _currentGoalPoints.value = _taskHeightTotal.value
//            taskRepository.updateTask()
    }

    fun decreaseTaskHeight() {
        _taskHeightTotal.value = --counterTaskHeight
        _currentGoalPoints.value = _taskHeightTotal.value
//        taskRepository.updateTask()
    }
}
