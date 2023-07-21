package com.n1.moguchi.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.data.repositories.ParentRepository
import com.n1.moguchi.data.repositories.TaskRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class CommonCreationDialogViewModel @Inject constructor(
    private val parentRepository: ParentRepository,
    private val goalRepository: GoalRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    private val _counter = MutableLiveData<Int>()
    val counter: LiveData<Int> = _counter

    private val _goalTitle = MutableLiveData<String>()
    val goalTitle: LiveData<String> = _goalTitle

    private var counterHeight = 1

    init {
        _counter.value = counterHeight
    }

    fun getChildrenList(parentId: String) {
        viewModelScope.launch {
            val children: List<Child> = parentRepository.getChildrenList(parentId)
            _children.value = children
        }
    }

    fun createGoal(goal: Goal, childId: String) {

    }

    fun createTask() {

    }

    fun increaseHeight() {
        if (counterHeight < 10) {
            _counter.value = ++counterHeight
        }
    }

    fun decreaseHeight() {
        if (counterHeight != 1) {
            _counter.value = --counterHeight
        }
    }
}