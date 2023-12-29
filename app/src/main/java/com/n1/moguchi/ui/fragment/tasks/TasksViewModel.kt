package com.n1.moguchi.ui.fragment.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.TaskRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _activeTasks = MutableLiveData<List<Task>>()
    val activeTasks: LiveData<List<Task>> = _activeTasks

    private val _completedTasks = MutableLiveData<List<Task>>()
    val completedTasks: LiveData<List<Task>> = _completedTasks

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
}
