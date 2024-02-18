package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.models.Task

interface GoalRepository {

    fun createGoal(goal: Goal, childId: String): Goal

    suspend fun getGoal(goalId: String): Goal

    suspend fun fetchChildGoals(childId: String): List<Goal>

    suspend fun fetchCompletedGoals(childId: String): List<Goal>

    suspend fun fetchTasks(goals: List<Goal>): Map<Goal, List<Task>>
}