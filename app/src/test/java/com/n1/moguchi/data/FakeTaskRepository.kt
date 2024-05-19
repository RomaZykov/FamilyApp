package com.n1.moguchi.data

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.n1.moguchi.data.models.remote.Task
import com.n1.moguchi.data.repositories.TaskRepository
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
        if (!task.taskCompleted) {
            activeTasks.removeIf { task.taskId == it.taskId }
            completedTasks.add(task)
        } else {
            completedTasks.removeIf { task.taskId == it.taskId }
            activeTasks.add(task)
        }
    }

    override suspend fun deleteTask(goalId: String, task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun saveTasksToDb(goalId: String, tasks: List<Task>) {
        TODO("Not yet implemented")
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
}