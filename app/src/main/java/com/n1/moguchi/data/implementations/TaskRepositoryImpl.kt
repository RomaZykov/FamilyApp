package com.n1.moguchi.data.implementations

import com.google.firebase.database.FirebaseDatabase
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.TaskRepository
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor() : TaskRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val tasksRef = database.getReference("tasks")

    override fun createTask(task: Task, goalId: String): Task {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override fun markTaskCompleted(taskId: String, isCompleted: Boolean) {
        TODO("Not yet implemented")
    }

//    fun retrieveCompletedTodos(userId: String?): TaskList? {
//        return TODO()
//    }
}