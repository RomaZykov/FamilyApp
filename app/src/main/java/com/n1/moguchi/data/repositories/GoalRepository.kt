package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.remote.Goal
import com.n1.moguchi.data.models.remote.Task

interface GoalRepository {
    suspend fun getGoal(goalId: String): Goal

    fun returnCreatedGoal(title: String, totalPoints: Int, childId: String): Goal

    suspend fun saveGoalWithTasksToDb(goal: Goal, tasks: List<Task>)

    suspend fun saveTasksToDb(goalId: String, tasks: List<Task>)

    suspend fun fetchChildGoals(childId: String): List<Goal>

    suspend fun fetchCompletedGoals(childId: String): List<Goal>

    suspend fun fetchTasks(goals: List<Goal>): Map<Goal, List<Task>>

    suspend fun updateGoal(goalId: String, taskHeight: Int)
}