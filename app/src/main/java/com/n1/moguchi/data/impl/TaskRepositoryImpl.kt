package com.n1.moguchi.data.impl

import com.google.firebase.database.FirebaseDatabase
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.TaskRepository
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor() : TaskRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val tasksRef = database.getReference("tasks")

    override fun createTask(task: Task, goalId: String): Task {
        val taskId: String = UUID.randomUUID().toString()
        val taskRefByGoalId = tasksRef.child(taskId)
        val newTask = task.copy(
            taskId = taskId,
            goalOwnerId = goalId
        )
        taskRefByGoalId.setValue(newTask)
        return newTask
    }

    override fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override fun markTaskCompleted(taskId: String, isCompleted: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getTasks(goalId: String): List<Task> {
        val tasksRefByGoalId = tasksRef.child(goalId)
        val tasks: MutableList<Task> = mutableListOf()
        tasksRefByGoalId.get().await().children.map {
            tasks.add(it.getValue(Task::class.java)!!)
        }
        return tasks
    }

//    fun retrieveCompletedTodos(userId: String?): TaskList? {
//        return TODO()
//    }
}