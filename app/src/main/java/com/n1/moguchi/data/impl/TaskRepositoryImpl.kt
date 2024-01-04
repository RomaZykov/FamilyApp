package com.n1.moguchi.data.impl

import com.google.firebase.database.FirebaseDatabase
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.data.repositories.TaskRepository
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    database: FirebaseDatabase
) : TaskRepository {

    private val tasksRef = database.getReference("tasks")

    override suspend fun createTask(task: Task, goalID: String): Task {
        val taskId: String = UUID.randomUUID().toString()
        val taskRefByGoalId = tasksRef.child(goalID).child(taskId)
        val newTask = task.copy(
            taskId = taskId,
            goalOwnerId = goalID,
            taskCompleted = false
        )
        taskRefByGoalId.setValue(newTask)
        return newTask
    }

    override suspend fun deleteTask(task: Task) {
        val taskRefById = tasksRef.child(task.taskId)
        taskRefById.removeValue()
    }

    override fun markTaskCompleted(taskID: String, isCompleted: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(task: Task): Task {
        val taskRefByGoalId = tasksRef.child(task.goalOwnerId!!).child(task.taskId)
        val updatedTask = task.copy(
            height = task.height,
            title = task.title
        )
        val taskValues = updatedTask.toMap()
        taskRefByGoalId.updateChildren(taskValues)
        return updatedTask
    }

    override suspend fun fetchActiveTasks(goalID: String): List<Task> {
        val tasksRefByCompletion = tasksRef
            .orderByChild("taskCompleted")
            .equalTo(false)
        val tasks: MutableList<Task> = mutableListOf()
        tasksRefByCompletion.get().await().children.map {
            tasks.add(it.getValue(Task::class.java)!!)
        }
        return tasks
    }

    override suspend fun fetchCompletedTasks(goalID: String): List<Task> {
        val completedTasksRefByGoalId = tasksRef
            .orderByChild("taskCompleted")
            .equalTo(true)
        val completedTasks: MutableList<Task> = mutableListOf()
        completedTasksRefByGoalId.get().await().children.map {
            completedTasks.add(it.getValue(Task::class.java)!!)
        }
        return completedTasks
    }
}