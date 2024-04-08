package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.remote.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun createTask(task: Task, goalId: String): Task

    fun returnCreatedTask(goalId: String): Task

    suspend fun updateTask(task: Task): Task

    suspend fun deleteTask(goalId: String, task: Task)

    fun markTaskCompleted(taskId: String, isCompleted: Boolean)

//    suspend fun fetchActiveTasks(goalId: String): List<Task>
    fun fetchActiveTasks(goalId: String): Flow<List<Task>>

    suspend fun fetchCompletedTasks(goalId: String): List<Task>
}