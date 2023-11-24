package com.n1.moguchi.ui.fragments.parent.task_creation

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
import javax.inject.Inject

private const val MAX_TASK_HEIGHT = 3
private const val MIN_TASK_HEIGHT = 1

class TaskCreationViewModel @Inject constructor(
    private val parentRepository: ParentRepository,
    private val goalRepository: GoalRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    private val _taskHeight = MutableLiveData<Int>()
    val taskHeight: LiveData<Int> = _taskHeight

    private val _taskName = MutableLiveData<String>()
    val taskName: LiveData<String> = _taskName

    private var counterTaskHeight = MIN_TASK_HEIGHT

    init {
        _taskHeight.value = counterTaskHeight
        _tasks.value = emptyList()
    }

    fun createTask(task: Task, goalId: String): Task {
        return taskRepository.createTask(task, goalId)
    }

    fun getTasksByGoalId(goalId: String) {
        viewModelScope.launch {
            val tasks: List<Task> = taskRepository.getTasks(goalId)
            _tasks.value = tasks.toList()
        }
    }

    fun increaseTaskHeight() {
        if (counterTaskHeight < MAX_TASK_HEIGHT) {
            _taskHeight.value = ++counterTaskHeight
        }
    }

    fun decreaseGoalHeight() {
        if (counterTaskHeight != MIN_TASK_HEIGHT) {
            _taskHeight.value = --counterTaskHeight
        }
    }
}