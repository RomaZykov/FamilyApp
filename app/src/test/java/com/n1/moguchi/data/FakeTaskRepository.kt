package com.n1.moguchi.data

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.n1.moguchi.data.models.remote.Task
import com.n1.moguchi.data.repositories.TaskRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class FakeTaskRepository : TaskRepository {

    private val _activeTasks = MutableLiveData<List<Task>>()
    val activeTasks: LiveData<List<Task>> = _activeTasks

    private val _completedTasks = MutableLiveData<List<Task>>()
    val completedTasks: LiveData<List<Task>> = _completedTasks

    override fun fetchAllTasks(goalId: String): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun fetchActiveTasks(goalId: String): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun fetchCompletedTasks(goalId: String): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun createTask(goalId: String): Task {
        val taskId = generateId()
        return Task(taskId = taskId, goalOwnerId = goalId)
    }

    override suspend fun updateTask(task: Task) {
        TODO("Not yet implemented")
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
            if (task.taskCompleted) {
                _activeTasks.value?.plus(task)
            } else {
                _completedTasks.value?.plus(task)
            }
        }
    }

    @VisibleForTesting
    fun getActiveTask(): Task {
        return if (_activeTasks.value?.isNotEmpty() == true) {
            _activeTasks.value!!.first()
        } else {
            throw Exception("Test exception")
        }
    }
}