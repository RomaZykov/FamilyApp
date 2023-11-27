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
import kotlinx.coroutines.runBlocking
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

    private val _goalHeight = MutableLiveData<Int>()
    val goalHeight: LiveData<Int> = _goalHeight

    private val _taskHeightTotal = MutableLiveData<Int>()
    val taskHeightTotal: LiveData<Int> = _taskHeightTotal

    private val _taskName = MutableLiveData<String>()
    val taskName: LiveData<String> = _taskName

    private var counterTaskHeight = MIN_TASK_HEIGHT

    init {
        _taskHeightTotal.value = counterTaskHeight
    }

    fun createTask(task: Task, goalId: String): Task {
        val result = runBlocking {
            taskRepository.createTask(task, goalId)
        }
        return result
    }

    fun setupMaxPointsOfGoal(goalId: String) {
        viewModelScope.launch {
            _goalHeight.postValue(goalRepository.getGoal(goalId).height)
        }
    }

//    fun removeTask(parentId: String, childId: String) {
//        _children.value =
//            _children.value?.dropWhile { it.childId == childId && it.parentOwnerId == parentId }
//        viewModelScope.launch {
//            parentRepository.deleteChildProfile(parentId, childId)
//        }
//    }

    fun getTasksByGoalId(goalId: String) {
        viewModelScope.launch {
            val tasks: List<Task> = taskRepository.getTasks(goalId)
            _tasks.value = (_tasks.value ?: emptyList()) + tasks
        }
    }

    fun increaseTaskHeight() {
        if (counterTaskHeight < MAX_TASK_HEIGHT) {
            _taskHeightTotal.value = ++counterTaskHeight
//            taskRepository.
        }
    }

    fun decreaseTaskHeight() {
        if (counterTaskHeight != MIN_TASK_HEIGHT) {
            _taskHeightTotal.value = --counterTaskHeight
        }
    }
}