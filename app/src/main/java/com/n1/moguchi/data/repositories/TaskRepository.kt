package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.Task

interface TaskRepository {
    suspend fun createTask(task: Task, goalId: String): Task

    suspend fun updateTask(task: Task): Task

    fun deleteTask(taskId: String)

    fun markTaskCompleted(taskId: String, isCompleted: Boolean)

    suspend fun getTasks(goalId: String): List<Task>
}