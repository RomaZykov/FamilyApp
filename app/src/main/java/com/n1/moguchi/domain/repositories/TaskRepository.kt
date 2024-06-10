package com.n1.moguchi.domain.repositories

import com.n1.moguchi.data.remote.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun fetchAllTasks(goalId: String): Flow<List<Task>>

    fun fetchActiveTasks(goalId: String): Flow<List<Task>>

    fun fetchCompletedTasks(goalId: String): Flow<List<Task>>

    fun createTask(goalId: String): Task

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(goalId: String, task: Task)

    suspend fun saveTasksToDb(goalId: String, tasks: List<Task>)
}