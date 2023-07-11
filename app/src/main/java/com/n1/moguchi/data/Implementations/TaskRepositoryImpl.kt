package com.n1.moguchi.data.Implementations

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.TaskRepository

class TaskRepositoryImpl : TaskRepository {

    private val database = Firebase.database
    val tasksRef = database.getReference("tasks")

    fun createTask(goalId: String): Task {
        TODO("Not yet implemented")
    }

    fun updateTask(taskId: String): Task {
        TODO("Not yet implemented")
    }

    fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    fun markTaskCompleted(taskId: String, isCompleted: Boolean) {
        TODO("Not yet implemented")
    }

//    fun retrieveCompletedTodos(userId: String?): TaskList? {
//        return TODO()
//    }
}