package com.n1.moguchi.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.n1.moguchi.data.models.Task
import javax.inject.Inject

class MainActivityViewModel @Inject constructor() : ViewModel() {

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task> = _task

    private val _isTaskCompleted = MutableLiveData<Boolean>()
    val isTaskCompleted: LiveData<Boolean> = _isTaskCompleted

    private val _isGoalCompleted = MutableLiveData<Boolean>()
    val isGoalCompleted: LiveData<Boolean> = _isGoalCompleted

    fun getTasksList() {

    }

    fun getGoalsList() {

    }

    private fun updateProgress() {

    }

    fun isAuthenticated(): Boolean {
        return TODO()
    }
}
