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

private const val MIN_HEIGHT = 1

class TaskCreationViewModel @Inject constructor(
    private val parentRepository: ParentRepository,
    private val goalRepository: GoalRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    private val _goalHeight = MutableLiveData<Int>()
    val goalHeight: LiveData<Int> = _goalHeight

    private val _taskHeightTotal = MutableLiveData<Int>()
    val taskHeightTotal: LiveData<Int> = _taskHeightTotal

    private val _taskName = MutableLiveData<String>()
    val taskName: LiveData<String> = _taskName

    private var counterTaskHeight = MIN_HEIGHT

    init {
        _taskHeightTotal.value = counterTaskHeight
    }

    fun createTask(task: Task, goalId: String): Task {
        val newTask = runBlocking {
            taskRepository.createTask(task, goalId)
        }
        _taskHeightTotal.value?.plus(newTask.height)
        _tasks.value = (_tasks.value ?: emptyList()) + newTask
        return newTask
    }

    fun setupMaxPointsOfGoal(goalId: String) {
        viewModelScope.launch {
            _goalHeight.postValue(goalRepository.getGoal(goalId).height)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
            _tasks.value =
                _tasks.value?.dropWhile { it.taskId == task.taskId }
            _taskHeightTotal.value?.minus(task.height)
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
//        taskRepository.updateTask()
    }

    fun decreaseTaskHeight() {
        _taskHeightTotal.value = --counterTaskHeight
//        taskRepository.updateTask()
    }
}