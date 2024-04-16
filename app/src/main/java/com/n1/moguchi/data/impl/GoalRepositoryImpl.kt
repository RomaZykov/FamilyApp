package com.n1.moguchi.data.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.n1.moguchi.data.models.remote.Goal
import com.n1.moguchi.data.models.remote.Task
import com.n1.moguchi.data.repositories.GoalRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    database: FirebaseDatabase,
    private val auth: FirebaseAuth
) : GoalRepository {

    private val goalsRef = database.getReference("goals")
    private val tasksRef = database.getReference("tasks")

    override fun fetchActiveGoals(childId: String): Flow<List<Goal>> = callbackFlow {
        val goalsListener = goalsRef.orderByChild("childOwnerId").equalTo(childId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val goals = mutableListOf<Goal>()
                    for (relatedGoal in snapshot.children) {
                        if (relatedGoal
                                .child("goalCompleted").getValue(Boolean::class.java) == false
                        ) {
                            val goal = relatedGoal.getValue(Goal::class.java)
                            if (goal != null) {
                                goals.add(goal)
                            }
                        }
                    }
                    trySend(if (goals.isEmpty()) emptyList() else goals)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        awaitClose { goalsRef.removeEventListener(goalsListener) }
    }

    override fun fetchCompletedGoals(childId: String): Flow<List<Goal>> = callbackFlow {
        val listener = goalsRef.orderByChild("childOwnerId").equalTo(childId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val completedGoals = mutableListOf<Goal>()
                    for (relatedGoal in snapshot.children) {
                        if (relatedGoal
                                .child("goalCompleted").getValue(Boolean::class.java) == true
                        ) {
                            val goal = relatedGoal.getValue(Goal::class.java)
                            if (goal != null) {
                                completedGoals.add(goal)
                            }
                        }
                    }
                    trySend(completedGoals)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        awaitClose { goalsRef.removeEventListener(listener) }
    }

    override suspend fun getGoal(goalId: String): Goal {
        val goal = goalsRef.child(goalId).get().await().getValue(Goal::class.java)
        return goal!!
    }

    override fun returnCreatedGoal(title: String, totalPoints: Int, childId: String): Goal {
        val goalId: String = UUID.randomUUID().toString()
        return Goal(
            goalId = goalId,
            title = title,
            totalPoints = totalPoints,
            childOwnerId = childId,
            parentOwnerId = auth.currentUser?.uid
        )
    }

    override suspend fun saveGoalWithTasksToDb(goal: Goal, tasks: List<Task>) {
        val goalId = goal.goalId!!
        goalsRef.child(goalId).setValue(goal)
        for (task in tasks) {
            if (task.goalOwnerId == goalId) {
                val taskRefByGoalId = tasksRef.child(goalId).child(task.taskId)
                taskRefByGoalId.setValue(task)
            }
        }
    }

    override suspend fun updateGoalStatus(goalId: String) {
        val goalRef = goalsRef.child(goalId)
        val goal = goalRef.get().await().getValue(Goal::class.java)
        val updatedGoal = goal?.copy(
            goalCompleted = !goal.goalCompleted
        )
        val goalValues = updatedGoal?.toMap()
        goalsRef.child(goalId).updateChildren(goalValues!!)
    }

    override suspend fun updateGoalPoints(goalId: String, taskHeight: Int) {
        val goalRef = goalsRef.child(goalId)
        val goal = goalRef.get().await().getValue(Goal::class.java)
        val currentGoalPoints = goal?.currentPoints
        val updatedGoal = goal?.copy(
            currentPoints = currentGoalPoints?.plus(taskHeight) ?: 0
        )
        val goalValues = updatedGoal?.toMap()
        goalsRef.child(goalId).updateChildren(goalValues!!)
    }
}