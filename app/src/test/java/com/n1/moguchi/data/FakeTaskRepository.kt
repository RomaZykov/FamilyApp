package com.n1.moguchi.data

import androidx.annotation.VisibleForTesting
import com.n1.moguchi.data.remote.model.Task
import com.n1.moguchi.domain.repositories.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class FakeTaskRepository : TaskRepository {

    private val activeTasks = mutableListOf<Task>()
    private val completedTasks = mutableListOf<Task>()

    override fun fetchAllTasks(goalId: String): Flow<List<Task>> = flow {
        val tasks = activeTasks + completedTasks
        emit(tasks)
    }

    override fun fetchActiveTasks(goalId: String): Flow<List<Task>> = flow {
        emit(activeTasks)
    }

    override fun fetchCompletedTasks(goalId: String): Flow<List<Task>> = flow {
        emit(completedTasks)
    }

    override fun createTask(goalId: String): Task {
        val taskId = generateId()
        return Task(taskId = taskId, goalOwnerId = goalId)
    }

    override suspend fun updateTask(task: Task) {
        println("Remote changes")
    }

    override suspend fun deleteTask(goalId: String, task: Task) {
        println("Remote changes")
    }

    override suspend fun saveTasksToDb(goalId: String, tasks: List<Task>) {
        println("Remote changes")
    }

    private fun generateId() = UUID.randomUUID().toString()

    @VisibleForTesting
    fun addTasks(tasks: List<Task>) {
        for (task in tasks) {
            if (!task.taskCompleted) {
                activeTasks.add(task)
            } else {
                completedTasks.add(task)
            }
        }
    }

    @VisibleForTesting
    fun getActiveTask(): Task {
        return if (activeTasks.isNotEmpty()) {
            activeTasks.first()
        } else {
            throw Exception("Test exception")
        }
    }

    @VisibleForTesting
    fun clearTasks() {
        activeTasks.removeAllList()
        completedTasks.removeAllList()
    }

    private fun <T> MutableList<T>.removeAllList() {
        if (this.isEmpty()) return
        repeat(this.size) {
            this.removeAt(0)
        }
    }
}