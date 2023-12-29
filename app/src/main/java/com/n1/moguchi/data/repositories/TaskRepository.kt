package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.Task

interface TaskRepository {
    suspend fun createTask(task: Task, goalID: String): Task

    suspend fun updateTask(task: Task): Task

    suspend fun deleteTask(task: Task)

    fun markTaskCompleted(taskID: String, isCompleted: Boolean)

    suspend fun fetchActiveTasks(goalID: String): List<Task>

    suspend fun fetchCompletedTasks(goalID: String): List<Task>
}