package com.n1.moguchi.data.implementations

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.domain.models.Goal
import com.n1.moguchi.domain.repositories.GoalRepository
import java.util.UUID

class GoalRepositoryImpl(userId: String?, private val goalRepository: GoalRepository) : GoalRepository {

    private val database = Firebase.database
    val parentRef = database.getReference("parents/")

    override fun createGoal(goal: Goal): Goal {
        goal.goalId = UUID.randomUUID().toString()
        return TODO()
    }

    override fun updateGoal(goalId: String): Goal {
        TODO("Not yet implemented")
    }

    override fun deleteGoal(goalId: String) {
        TODO("Not yet implemented")
    }
}