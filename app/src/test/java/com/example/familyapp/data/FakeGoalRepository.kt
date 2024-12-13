package com.example.familyapp.data

import androidx.annotation.VisibleForTesting
import com.example.familyapp.data.remote.model.Goal
import com.example.familyapp.data.remote.model.Task
import com.example.familyapp.domain.repositories.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeGoalRepository : GoalRepository {
    private val activeGoals = mutableListOf<Goal>()
    private val completedGoals = mutableListOf<Goal>()

    override fun fetchActiveGoals(childId: String): Flow<List<Goal>> = flow {
        emit(activeGoals)
    }

    override fun fetchCompletedGoals(childId: String): Flow<List<Goal>?> {
        TODO("Not yet implemented")
    }

    override suspend fun getGoal(goalId: String): Goal {
        val goal = (activeGoals + completedGoals).find { it.goalId == goalId }
        return goal!!
    }

    override fun returnCreatedGoal(title: String, totalPoints: Int, childId: String): Goal {
        TODO("Not yet implemented")
    }

    override suspend fun saveGoalWithTasksToDb(goal: Goal, tasks: List<Task>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateGoalPoints(goalId: String, taskHeight: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateGoalStatus(goalId: String) {
        TODO("Not yet implemented")
    }

    @VisibleForTesting
    fun addGoals(goals: List<Goal>) {
        for (goal in goals) {
            if (!goal.goalCompleted) {
                activeGoals.add(goal)
            } else {
                completedGoals.add(goal)
            }
        }
    }
}