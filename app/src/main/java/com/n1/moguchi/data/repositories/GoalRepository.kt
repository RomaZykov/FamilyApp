package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.remote.Goal
import com.n1.moguchi.data.models.remote.Task
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    // Goals
//    fun fetchActiveGoals(childId: String): Flow<List<Goal>?>
    fun fetchActiveGoals(childId: String): Flow<List<Goal>?>
    fun fetchCompletedGoals(childId: String): Flow<List<Goal>?>
    suspend fun getGoal(goalId: String): Goal
    fun returnCreatedGoal(title: String, totalPoints: Int, childId: String): Goal
    suspend fun saveGoalWithTasksToDb(goal: Goal, tasks: List<Task>)
    suspend fun updateGoalPoints(goalId: String, taskHeight: Int)
    suspend fun updateGoalStatus(goalId: String)


    // Tasks
//    suspend fun fetchTasks(goals: List<Goal>): Map<Goal, List<Task>>
    suspend fun saveTasksToDb(goalId: String, tasks: List<Task>)
}