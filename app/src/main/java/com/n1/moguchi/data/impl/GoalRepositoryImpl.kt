package com.n1.moguchi.data.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.GoalRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor() : GoalRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val auth: FirebaseAuth = Firebase.auth
    private val goalsRef = database.getReference("goals")
    private val tasksRef = database.getReference("tasks")

    override fun createGoal(goal: Goal, childId: String): Goal {
        val goalsRefByChildId = goalsRef.child(goal.goalId!!)
        val newGoal = goal.copy(
            childOwnerId = childId,
            parentOwnerId = auth.currentUser?.uid
        )
        goalsRefByChildId.setValue(newGoal)
        return newGoal
    }

    override suspend fun getChildGoals(childID: String): List<Goal> {
        val goalsRefByChildId = goalsRef.orderByChild("childOwnerId").equalTo(childID)
        val goals = mutableListOf<Goal>()
        goalsRefByChildId.get().await().children.forEach { goal ->
            goals.add(goal.getValue(Goal::class.java)!!)

        }
        return goals
    }

    override suspend fun updateGoal(goalID: String) {
        TODO("Not yet implemented")
//        val specificGoalRef = goalsRef.child(goalID)
//        val goalRefByTasksId = tasksRef.orderByChild("goalOwnerId").equalTo(goalID)
//        val specificGoal = goalsRef.child(goalID).get().await()
//        val tasks = mutableListOf<Task>()
//        val updatedGoal = specificGoal.value as Goal
//        updatedGoal.taskList.apply {
//            goalRefByTasksId.get().await().children.forEach {
//                tasks.add(it.getValue(Task::class.java)!!)
//            }
//        }
//        val goalValues = updatedGoal.toMap()
//        specificGoalRef.updateChildren(goalValues)
    }

    override suspend fun getGoal(goalID: String): Goal {
        val goal = goalsRef.child(goalID).get().await().getValue(Goal::class.java)
        return goal!!
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