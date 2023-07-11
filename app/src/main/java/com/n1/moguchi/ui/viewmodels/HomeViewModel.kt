package com.n1.moguchi.ui.viewmodels

import com.n1.moguchi.data.Implementations.GoalRepositoryImpl
import com.n1.moguchi.data.Implementations.ParentRepositoryImpl
import com.n1.moguchi.data.Implementations.TaskRepositoryImpl
import com.n1.moguchi.data.models.Goal

class HomeViewModel(
    private val parentRepositoryImpl: ParentRepositoryImpl,
    private val goalRepositoryImpl: GoalRepositoryImpl,
    private val taskRepositoryImpl: TaskRepositoryImpl
) {

//    private val _playlist = MutableLiveData<List<Video>>()
//    val playlist: LiveData<List<Video>> = _playlist

    fun createGoal(): Goal {
        return goalRepositoryImpl.createGoal()
    }

    fun createTask() {

    }

    fun getTasksList() {

    }

    fun getGoalsList() {

    }

}