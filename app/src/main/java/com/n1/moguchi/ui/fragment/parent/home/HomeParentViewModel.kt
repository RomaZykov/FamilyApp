package com.n1.moguchi.ui.fragment.parent.home

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.models.Parent
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.data.repositories.ParentRepository
import com.n1.moguchi.interactors.FetchParentDataUseCase
import com.n1.moguchi.ui.ProfileImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeParentViewModel @Inject constructor(
    private val parentRepository: ParentRepository,
    private val goalRepository: GoalRepository,
    private val fetchParentDataUseCase: FetchParentDataUseCase,
    private val profileImage: ProfileImage
) : ViewModel() {

    private val _children = MutableLiveData<Map<String, Child>>()
    val children: LiveData<Map<String, Child>> = _children

    private val _goals = MutableLiveData<Map<Goal, List<Task>>>()
    val goals: LiveData<Map<Goal, List<Task>>> = _goals

    private val _totalTasks = MutableLiveData<Int>()
    val totalTasks: LiveData<Int> = _totalTasks

    private val _completedGoals = MutableLiveData<List<Goal>>()
    val completedGoals: LiveData<List<Goal>> = _completedGoals

    fun fetchParentData(parentId: String): Flow<Parent> {
        return fetchParentDataUseCase.invoke(parentId).map {
            it!!
        }
    }

    fun load(url: String, drawable: MenuItem) = profileImage.load(url, drawable)

    fun fetchChildren(parentId: String) {
        viewModelScope.launch {
            val children: Map<String, Child> = parentRepository.fetchChildren(parentId)
            _children.value = children
        }
    }

    fun fetchGoalsAndTasks(childID: String) {
        viewModelScope.launch {
            val goals = goalRepository.fetchChildGoals(childID)
            val goalsWithTasksMap = goalRepository.fetchTasks(goals)
            _goals.value = goalsWithTasksMap
        }
    }

    fun fetchCompletedGoals(childID: String) {
        viewModelScope.launch {
            val completedGoals = goalRepository.fetchCompletedGoals(childID)
            _completedGoals.value = completedGoals
        }
    }
}