package com.n1.moguchi.ui.fragment.child.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.n1.moguchi.data.models.local.UserPreferences
import com.n1.moguchi.data.models.remote.Child
import com.n1.moguchi.data.models.remote.Goal
import com.n1.moguchi.data.models.remote.Task
import com.n1.moguchi.data.repositories.AppRepository
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.interactors.FetchChildDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeChildViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val appRepository: AppRepository,
    private val fetchChildDataUseCase: FetchChildDataUseCase
) : ViewModel() {

    private val _goals = MutableLiveData<Map<Goal, List<Task>>>()
    val goals: LiveData<Map<Goal, List<Task>>> = _goals

    private val _totalTasks = MutableLiveData<Int>()
    val totalTasks: LiveData<Int> = _totalTasks

    private val _completedGoals = MutableLiveData<List<Goal>>()
    val completedGoals: LiveData<List<Goal>> = _completedGoals

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

//    fun fetchActiveGoalsWithTasks(childId: String): Flow<Map<Goal, List<Task>>> {
//        return fetchActiveGoalsUseCase.invoke(childId).map {
//            it!!
//        }
//    }
//        viewModelScope.launch {
//            val goals = goalRepository.fetchActiveGoals(childId)
//            val goalsWithTasksMap = goalRepository.fetchTasks(goals)
//            _goals.value = goalsWithTasksMap
//        }

//    fun fetchCompletedGoals(childId: String): Flow<List<Goal>?> {
//        return fetchCompletedGoalsUseCase.invoke(childId).map {
//            it
//        }
//    }
}