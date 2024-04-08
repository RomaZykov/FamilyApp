package com.n1.moguchi.ui.fragment.parent.home

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.remote.Child
import com.n1.moguchi.data.models.remote.Goal
import com.n1.moguchi.data.models.remote.Task
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.data.repositories.ParentRepository
import com.n1.moguchi.data.repositories.TaskRepository
import com.n1.moguchi.interactors.FetchCompletedGoalsUseCase
import com.n1.moguchi.interactors.FetchParentDataUseCase
import com.n1.moguchi.ui.ProfileImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeParentViewModel @Inject constructor(
    private val parentRepository: ParentRepository,
    private val goalRepository: GoalRepository,
    private val taskRepository: TaskRepository,
    private val fetchParentDataUseCase: FetchParentDataUseCase,
    private val fetchCompletedGoalsUseCase: FetchCompletedGoalsUseCase,
    private val profileImage: ProfileImage
) : ViewModel() {

    private val _children = MutableLiveData<Map<String, Child>>()
    val children: LiveData<Map<String, Child>> = _children

    private val _selectedChild = MutableLiveData<Child>()
    val selectedChild: LiveData<Child> = _selectedChild

    private val _totalTasks = MutableLiveData<Int>()
    val totalTasks: LiveData<Int> = _totalTasks

    private val _completedGoals = MutableLiveData<List<Goal>>()
    val completedGoals: LiveData<List<Goal>> = _completedGoals

    private val _goalsWithTasks = MutableSharedFlow<Map<Goal, List<Task>>>()
    val goalsWithTasks: SharedFlow<Map<Goal, List<Task>>> = _goalsWithTasks.asSharedFlow()

    fun fetchActiveGoalsWithTasks(childId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            goalRepository.fetchActiveGoals(childId)
                .filterNotNull()
                .flatMapConcat { goals ->
                    combine(goals.map { taskRepository.fetchActiveTasks(it.goalId!!) }) {
                        goals.zip(it)
                    }
                }.collect { goalsWithTasksList ->
                    _goalsWithTasks.emit(goalsWithTasksList.toMap())
                }
        }
    }

    fun load(url: String, drawable: MenuItem) = profileImage.load(url, drawable)

    fun fetchChildren(parentId: String) {
        viewModelScope.launch {
            val children: Map<String, Child> = parentRepository.fetchChildren(parentId)
            _selectedChild.value = children.values.first()
            _children.value = children
        }
    }

//    fun fetchActiveGoalsWithTasks(childId: String) =
//        goalRepository.fetchActiveGoals(childId).flowOn(Dispatchers.IO)

    fun fetchCompletedGoals(childId: String): Flow<List<Goal>> {
        return fetchCompletedGoalsUseCase.invoke(childId).map {
            it!!
        }
    }

    fun selectAnotherChild(child: Child) {
        _selectedChild.value = _children.value?.get(child.childId)
    }
//        viewModelScope.launch {
//            val completedGoals = goalRepository.fetchCompletedGoals(childID)
//            _completedGoals.value = completedGoals
//        }
//    }
}