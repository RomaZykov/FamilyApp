package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.Task

interface TaskRepository {

    suspend fun createTask(task: Task, goalId: String): Task

    fun returnCreatedTask(goalId: String): Task

    suspend fun updateTask(task: Task): Task

    suspend fun deleteTask(goalId: String, task: Task)

    fun markTaskCompleted(taskId: String, isCompleted: Boolean)

    suspend fun fetchActiveTasks(goalId: String): List<Task>

    suspend fun fetchCompletedTasks(goalId: String): List<Task>
}