package com.n1.moguchi.data.implementations

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.domain.models.goals.Goal
import com.n1.moguchi.domain.repositories.GoalRepository
import java.util.UUID

class GoalRepositoryImpl(private val goalRepository: GoalRepository) : GoalRepository {

    val database = Firebase.database

    override fun createGoal(goal: Goal): Goal {
        if (goal.goalId == null) {
            goal.goalId = UUID.randomUUID().toString()
        }
//        localRepository.save(task)
        return TODO()
    }

    override fun updateGoal(goalId: String): Goal {
        TODO("Not yet implemented")
    }

    override fun deleteGoal(goalId: String) {
        TODO("Not yet implemented")
    }
}