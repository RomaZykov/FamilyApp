package com.example.familyapp.ui.fragment.child.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familyapp.data.local.UserPreferences
import com.example.familyapp.data.remote.model.Child
import com.example.familyapp.data.remote.model.Goal
import com.example.familyapp.data.remote.model.Task
import com.example.familyapp.domain.repositories.AppRepository
import com.example.familyapp.domain.repositories.GoalRepository
import com.example.familyapp.domain.repositories.TaskRepository
import com.example.familyapp.domain.usecases.FetchChildDataUseCase
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