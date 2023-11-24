package com.n1.moguchi.data.implementations

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.repositories.GoalRepository
import java.util.UUID
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor() : GoalRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val auth: FirebaseAuth = Firebase.auth
    private val goalsRef = database.getReference("goals")

    override fun createGoal(goal: Goal, childId: String): Goal {
        val goalId: String = UUID.randomUUID().toString()
        val goalsRefByChildId = goalsRef.child(goalId)
        val newGoal = goal.copy(
            goalId = goalId,
            childOwnerId = childId,
            parentOwnerId = auth.currentUser?.uid
        )
        goalsRefByChildId.setValue(newGoal)
        return newGoal
    }

//    override suspend fun getTasks(goalId: String): List<Task> {
//        val tasksRefByGoalId = goalsRef.child(goalId)
//        val tasks: MutableList<Task> = mutableListOf()
//        tasksRefByGoalId.get().await().children.map {
//            tasks.add(it.getValue(Task::class.java)!!)
//        }
//        return tasks
//    }
}