package com.n1.moguchi.domain.repositories

import com.n1.moguchi.domain.models.goals.Goal

interface GoalRepository {

    fun createGoal(): Goal

    fun updateGoal(): Goal

    fun deleteGoal()
}