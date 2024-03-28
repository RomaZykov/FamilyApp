package com.n1.moguchi.data.impl

import com.google.firebase.database.FirebaseDatabase
import com.n1.moguchi.data.models.remote.Task
import com.n1.moguchi.data.repositories.TaskRepository
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    database: FirebaseDatabase
) : TaskRepository {

    private val tasksRef = database.getReference("tasks")

    override fun returnCreatedTask(goalId: String): Task {
        val taskId: String = UUID.randomUUID().toString()
        return Task(
            taskId = taskId,
            goalOwnerId = goalId,
            taskCompleted = false
        )
    }

    override suspend fun createTask(task: Task, goalId: String): Task {
        val taskId: String = UUID.randomUUID().toString()
        val taskRefByGoalId = tasksRef.child(goalId).child(taskId)
        val newTask = task.copy(
            taskId = taskId,
            goalOwnerId = goalId,
            taskCompleted = false
        )
        taskRefByGoalId.setValue(newTask)
        return newTask
    }

    override suspend fun deleteTask(goalId: String, task: Task) {
        val taskRefById = tasksRef.child(goalId).child(task.taskId)
        taskRefById.removeValue()
    }

    override fun markTaskCompleted(taskId: String, isCompleted: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(task: Task): Task {
        val taskRefByGoalId = tasksRef.child(task.goalOwnerId!!).child(task.taskId)
        val updatedTask = task.copy(
            height = task.height,
            title = task.title,
            taskCompleted = task.taskCompleted
        )
        val taskValues = updatedTask.toMap()
        taskRefByGoalId.updateChildren(taskValues)
        return updatedTask
    }

    override suspend fun fetchActiveTasks(goalId: String): List<Task> {
        val tasksRefByCompletion = tasksRef.child(goalId)
            .orderByChild("taskCompleted")
            .equalTo(false)
        val tasks: MutableList<Task> = mutableListOf()
        tasksRefByCompletion.get().await().children.map {
            tasks.add(it.getValue(Task::class.java)!!)
        }
        return tasks
    }

    override suspend fun fetchCompletedTasks(goalId: String): List<Task> {
        val completedTasksRefByGoalId = tasksRef.child(goalId)
            .orderByChild("taskCompleted")
            .equalTo(true)
        val completedTasks: MutableList<Task> = mutableListOf()
        completedTasksRefByGoalId.get().await().children.map {
            completedTasks.add(it.getValue(Task::class.java)!!)
        }
        return completedTasks
    }
}