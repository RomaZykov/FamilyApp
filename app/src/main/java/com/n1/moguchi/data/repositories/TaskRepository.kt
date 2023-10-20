package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.Task

interface TaskRepository {
    fun createTask(task: Task, goalId: String): Task

    fun deleteTask(taskId: String)

    fun markTaskCompleted(taskId: String, isCompleted: Boolean)

    fun getTasks(goalId: String): List<Task>
}