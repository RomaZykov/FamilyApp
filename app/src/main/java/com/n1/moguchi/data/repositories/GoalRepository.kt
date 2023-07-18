package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.models.Task

interface GoalRepository {

    fun createGoal(goal: Goal): Goal

    fun getTasks(goalId: String): List<Task>
}