package com.n1.moguchi.data

import com.n1.moguchi.data.models.remote.Goal
import com.n1.moguchi.data.models.remote.Task
import com.n1.moguchi.data.repositories.GoalRepository
import kotlinx.coroutines.flow.Flow

class FakeGoalRepository : GoalRepository {
    override fun fetchActiveGoals(childId: String): Flow<List<Goal>> {
        TODO("Not yet implemented")
    }

    override fun fetchCompletedGoals(childId: String): Flow<List<Goal>?> {
        TODO("Not yet implemented")
    }

    override suspend fun getGoal(goalId: String): Goal {
        TODO("Not yet implemented")
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

    override suspend fun updateSecondaryGoalPoints(goalId: String, taskHeight: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateGoalStatus(goalId: String) {
        TODO("Not yet implemented")
    }
}