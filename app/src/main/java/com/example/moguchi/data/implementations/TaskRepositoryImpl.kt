package com.example.moguchi.data.implementations

import com.example.moguchi.domain.models.tasks.Task
import com.example.moguchi.domain.repositories.TaskRepository

class TaskRepositoryImpl : TaskRepository {
    override fun createTask(): Task {
        TODO("Not yet implemented")
    }

    override fun updateTask(): Task {
        TODO("Not yet implemented")
    }

    override fun deleteTask() {
        TODO("Not yet implemented")
    }

    override fun markTaskCompleted(taskId: Int, isCompleted: Boolean) {
        TODO("Not yet implemented")
    }
}