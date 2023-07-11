package com.n1.moguchi.data.Implementations

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.GoalRepository

class GoalRepositoryImpl : GoalRepository {

    private val database = Firebase.database
    val goalsRef = database.getReference("goals")

    fun createGoal(): Goal {

        return TODO()
    }

    fun getTasks(goalId: String): List<Task> {
        TODO("Not yet implemented")
    }
}