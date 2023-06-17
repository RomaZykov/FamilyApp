package com.example.moguchi.domain.repositories

import com.example.moguchi.domain.models.tasks.Task

interface TaskRepository {
    fun createTask(): Task

    fun updateTask(): Task

    fun deleteTask()

    fun markTaskCompleted(taskId: Int, isCompleted: Boolean)
}