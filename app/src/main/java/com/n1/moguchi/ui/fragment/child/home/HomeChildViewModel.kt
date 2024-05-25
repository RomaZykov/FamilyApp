package com.n1.moguchi.ui.fragment.child.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.local.UserPreferences
import com.n1.moguchi.data.remote.model.Child
import com.n1.moguchi.data.remote.model.Goal
import com.n1.moguchi.data.remote.model.Task
import com.n1.moguchi.data.repositories.AppRepository
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.data.repositories.TaskRepository
import com.n1.moguchi.interactors.FetchChildDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeChildViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val taskRepository: TaskRepository,
    private val appRepository: AppRepository,
    private val fetchChildDataUseCase: FetchChildDataUseCase
) : ViewModel() {

    private val _activeGoalsWithTasks = MutableSharedFlow<Map<Goal, List<Task>>>()
    val activeGoalsWithTasks: SharedFlow<Map<Goal, List<Task>>> = _activeGoalsWithTasks

    private val _completedGoalsWithTasks = MutableSharedFlow<Map<Goal, List<Task>>>()
    val completedGoalsWithTasks: SharedFlow<Map<Goal, List<Task>>> = _completedGoalsWithTasks

    fun getUserPrefs(): Flow<UserPreferences> {
        return appRepository.getUserPrefs().map {
            it
        }
    }

    fun fetchChildData(childId: String): Flow<Child> {
        return fetchChildDataUseCase.invoke(childId).map {
            it!!
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
}