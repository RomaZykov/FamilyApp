package com.example.moguchi.domain.repositories

import com.example.moguchi.domain.models.goals.Goal

interface GoalRepository {

    fun createGoal(): Goal

    fun updateGoal(): Goal

    fun deleteGoal()
}