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

    private val _taskHeightTotal = MutableLiveData<Int>()
    val taskHeightTotal: LiveData<Int> = _taskHeightTotal

    private val _taskName = MutableLiveData<String>()
    val taskName: LiveData<String> = _taskName

    init {
        _taskHeightTotal.value = 0
    }

    fun createTask(task: Task, goalId: String): Task {
        val newTask = runBlocking {
            taskRepository.createTask(task, goalId)
        }
        viewModelScope.launch {
            _taskHeightTotal.value = _taskHeightTotal.value?.plus(newTask.height)
            _tasks.value = (_tasks.value ?: emptyList()) + newTask
        }
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
            _taskHeightTotal.value = _taskHeightTotal.value?.minus(task.height)
        }
    }

    fun getTasksByGoalId(goalId: String) {
        viewModelScope.launch {
            val tasks: List<Task> = taskRepository.getTasks(goalId)
            _tasks.value = (_tasks.value ?: emptyList()) + tasks
        }
    }

    fun updateTask(task: Task, taskPointsChanged: Boolean, taskTitleChanged: Boolean) {
        viewModelScope.launch {
            if (!taskTitleChanged) {
                if (taskPointsChanged) {
                    _taskHeightTotal.value =
                        _taskHeightTotal.value?.plus(1)
                } else {
                    _taskHeightTotal.value =
                        _taskHeightTotal.value?.minus(1)
                }
            }
            taskRepository.updateTask(task)
        }
    }
}
