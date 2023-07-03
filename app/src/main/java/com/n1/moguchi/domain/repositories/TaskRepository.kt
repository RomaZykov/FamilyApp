package com.n1.moguchi.domain.repositories

import com.n1.moguchi.domain.models.Task

interface TaskRepository {
    fun createTask(): Task

    fun updateTask(): Task

    fun deleteTask()

    fun markTaskCompleted(taskId: Int, isCompleted: Boolean)
}