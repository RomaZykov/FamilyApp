package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.Goal

interface GoalRepository {

    fun createGoal(goal: Goal, childId: String): Goal

}