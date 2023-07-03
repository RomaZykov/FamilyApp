package com.n1.moguchi.data.implementations

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.domain.models.Task
import com.n1.moguchi.domain.repositories.TaskRepository

class TaskRepositoryImpl : TaskRepository {

    private val database = Firebase.database
    val parentRef = database.getReference("parents")

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

//    fun retrieveCompletedTodos(userId: String?): TaskList? {
//        return TODO()
//    }
}