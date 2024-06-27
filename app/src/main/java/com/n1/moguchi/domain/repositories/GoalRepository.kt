package com.n1.moguchi.domain.repositories

import com.n1.moguchi.data.remote.model.Goal
import com.n1.moguchi.data.remote.model.Task
import kotlinx.coroutines.flow.Flow

interface GoalRepository {

    fun fetchActiveGoals(childId: String): Flow<List<Goal>>

    fun fetchCompletedGoals(childId: String): Flow<List<Goal>?>

    suspend fun getGoal(goalId: String): Goal

    fun returnCreatedGoal(title: String, totalPoints: Int, childId: String): Goal

    suspend fun saveGoalWithTasksToDb(goal: Goal, tasks: List<Task>)

    suspend fun updateGoalPoints(goalId: String, taskHeight: Int)

    suspend fun updateGoalStatus(goalId: String)
}