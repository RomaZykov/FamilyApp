package com.n1.moguchi.data.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.GoalRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    database: FirebaseDatabase,
    private val auth: FirebaseAuth
) : GoalRepository {

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

    override suspend fun fetchTasks(goals: List<Goal>): Map<Goal, List<Task>> {
        val goalsWithTasks = mutableMapOf<Goal, List<Task>>()
        goals.forEach { goal ->
            val tasksRefByGoalId = tasksRef.child(goal.goalId!!)
            val tasks = mutableListOf<Task>()
            tasksRefByGoalId.get().await().children.forEach { task ->
                tasks.add(task.getValue(Task::class.java)!!)
            }
            goalsWithTasks[goal] = tasks
        }
        return goalsWithTasks
    }

    override suspend fun getGoal(goalID: String): Goal {
        val goal = goalsRef.child(goalID).get().await().getValue(Goal::class.java)
        return goal!!
    }

    override suspend fun getCompletedGoals(childID: String): List<Goal> {
        val completedGoalsRef = goalsRef.orderByChild("goalCompleted").equalTo(true)
        val completedGoals = mutableListOf<Goal>()
        completedGoalsRef.get().await().children.forEach { completedGoal ->
            completedGoals.add(completedGoal.getValue(Goal::class.java)!!)
        }
        return completedGoals
    }
}
