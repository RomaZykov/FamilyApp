package com.n1.moguchi.data.implementations

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.GoalRepository
import java.util.UUID
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor() : GoalRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val auth: FirebaseAuth = Firebase.auth
    private val goalsRef = database.getReference("goals")

    override fun createGoal(goal: Goal): Goal {
        if (goal.goalId == null) {
            goal.goalId = UUID.randomUUID().toString()
        }
        goal.parentOwnerId = auth.currentUser?.uid
        goalsRef.child(goal.goalId!!).setValue(goal)
            .addOnSuccessListener {
                // Write was successful!
            }
            .addOnFailureListener {
                // Write failed
            }
        return goal
    }

    override fun getTasks(goalId: String): List<Task> {
        TODO("Not yet implemented")
    }
}