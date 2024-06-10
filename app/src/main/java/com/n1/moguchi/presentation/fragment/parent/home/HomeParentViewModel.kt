package com.n1.moguchi.presentation.fragment.parent.home

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.remote.model.Child
import com.n1.moguchi.data.remote.model.Goal
import com.n1.moguchi.data.remote.model.Task
import com.n1.moguchi.domain.repositories.GoalRepository
import com.n1.moguchi.domain.repositories.ParentRepository
import com.n1.moguchi.domain.repositories.TaskRepository
import com.n1.moguchi.presentation.ProfileImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeParentViewModel @Inject constructor(
    private val parentRepository: ParentRepository,
    private val goalRepository: GoalRepository,
    private val taskRepository: TaskRepository,
    private val profileImage: ProfileImage
) : ViewModel() {

    private val _children = MutableLiveData<Map<String, Child>>()
    val children: LiveData<Map<String, Child>> = _children

    private val _selectedChild = MutableLiveData<Child>()
    val selectedChild: LiveData<Child> = _selectedChild

    private val _activeGoalsWithTasks = MutableSharedFlow<Map<Goal, List<Task>>>()
    val activeGoalsWithTasks: SharedFlow<Map<Goal, List<Task>>> = _activeGoalsWithTasks

    private val _completedGoalsWithTasks = MutableSharedFlow<Map<Goal, List<Task>>>()
    val completedGoalsWithTasks: SharedFlow<Map<Goal, List<Task>>> = _completedGoalsWithTasks

    fun load(url: String, drawable: MenuItem) = profileImage.load(url, drawable)

    fun fetchChildren(parentId: String) {
        viewModelScope.launch {
            val children: Map<String, Child> = parentRepository.fetchChildren(parentId)
            _selectedChild.value = children.values.first()
            _children.value = children
        }
    }

    fun fetchActiveGoalsWithTasks(childId: String) {
        viewModelScope.launch {
            goalRepository.fetchActiveGoals(childId)
                .filterNotNull()
                .flowOn(Dispatchers.IO)
                .flatMapLatest { goals ->
                    combine(goals.map { taskRepository.fetchActiveTasks(it.goalId!!) }) {
                        goals.zip(it)
                    }.onEmpty {
                        _activeGoalsWithTasks.emit(emptyMap())
                    }
                }.collect { goalsWithTasksList ->
                    _activeGoalsWithTasks.emit(goalsWithTasksList.toMap())
                }
        }
    }

    fun fetchCompletedGoalsWithTasks(childId: String) {
        viewModelScope.launch {
            goalRepository.fetchCompletedGoals(childId)
                .filterNotNull()
                .flowOn(Dispatchers.IO)
                .flatMapLatest { goals ->
                    combine(goals.map { taskRepository.fetchCompletedTasks(it.goalId!!) }) {
                        goals.zip(it)
                    }.onEmpty {
                        _completedGoalsWithTasks.emit(emptyMap())
                    }
                }.collect { goalsWithTasksList ->
                    _completedGoalsWithTasks.emit(goalsWithTasksList.toMap())
                }
        }
    }

    fun selectAnotherChild(child: Child) {
        _selectedChild.value = _children.value?.get(child.childId)
    }
}