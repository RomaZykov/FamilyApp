package com.n1.moguchi.domain.repositories

import com.n1.moguchi.domain.models.goals.Goal

interface GoalRepository {

    fun createGoal(goal: Goal): Goal

    fun updateGoal(goalId: String): Goal

    fun deleteGoal(goalId: String)
}