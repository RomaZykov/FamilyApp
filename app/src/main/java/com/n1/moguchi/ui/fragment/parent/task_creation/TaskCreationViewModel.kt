package com.n1.moguchi.ui.fragment.parent.task_creation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.data.repositories.TaskRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TaskCreationViewModel @Inject constructor(
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

    private val tasksList = mutableListOf<Task>()

    init {
        _tasks.value = tasksList
        _taskHeightTotal.value = 0
    }

    fun returnCreatedTask(goalId: String): Task {
        val preparedTask = taskRepository.returnCreatedTask(goalId)
        tasksList.add(preparedTask)
        _tasks.value = tasksList
        return tasksList.last()
    }

    fun createTask(task: Task, goalId: String): Task {
        val newTask = runBlocking {
            taskRepository.createTask(task, goalId)
        }
        tasksList.add(newTask)
        viewModelScope.launch {
            _taskHeightTotal.value = _taskHeightTotal.value?.plus(newTask.height)
            _tasks.value = tasksList
        }
        return newTask
    }

    fun setupGoal(relatedGoal: Goal) {
        _totalGoalPoints.value = relatedGoal.totalPoints
    }

    fun setupMaxPointsOfGoal(goalId: String) {
        viewModelScope.launch {
            _totalGoalPoints.postValue(goalRepository.getGoal(goalId).totalPoints)
        }
    }

    fun deleteTask(goalId: String, task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(goalId, task)
            _tasks.value =
                _tasks.value?.dropWhile { it.taskId == task.taskId }
            _taskHeightTotal.value = _taskHeightTotal.value?.minus(task.height)
        }
    }

    fun getTasksByGoalId(goalId: String) {
        viewModelScope.launch {
            val tasks: List<Task> = taskRepository.fetchActiveTasks(goalId)
            _tasks.value = (_tasks.value ?: emptyList()) + tasks
        }
    }

    fun onTaskUpdate(task: Task, taskPointsChanged: Boolean) {
        _tasks.value?.find {
            it.taskId == task.taskId
        }.also {
            if (taskPointsChanged) {
                _taskHeightTotal.value =
                    _taskHeightTotal.value?.plus(1)
            } else {
                _taskHeightTotal.value =
                    _taskHeightTotal.value?.minus(1)
            }
            it?.title = task.title
            it?.height = task.height
        }
    }
}
