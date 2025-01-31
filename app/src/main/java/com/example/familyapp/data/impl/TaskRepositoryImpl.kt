package com.example.familyapp.data.impl

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.familyapp.data.remote.model.Task
import com.example.familyapp.domain.repositories.TaskRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    database: FirebaseDatabase
) : TaskRepository {

    private val tasksRef = database.getReference("tasks")

    override fun createTask(goalId: String): Task {
        val taskId: String = UUID.randomUUID().toString()
        return Task(
            taskId = taskId,
            title = "",
            goalOwnerId = goalId,
            taskCompleted = false,
            onCheck = false,
            height = 1
        )
    }

    override suspend fun saveTasksToDb(goalId: String, tasks: List<Task>) {
        for (task in tasks) {
            if (task.goalOwnerId == goalId) {
                val taskRefByGoalId = tasksRef.child(goalId).child(task.taskId)
                taskRefByGoalId.setValue(task)
            }
        }
    }

    override suspend fun deleteTask(goalId: String, task: Task) {
        val taskRefById = tasksRef.child(goalId).child(task.taskId)
        taskRefById.removeValue()
    }

    override suspend fun updateTask(task: Task) {
        val taskRefByGoalId = tasksRef.child(task.goalOwnerId!!).child(task.taskId)
        val updatedTask = task.copy(
            height = task.height,
            title = task.title,
            taskCompleted = task.taskCompleted,
            onCheck = task.onCheck
        )
        val taskValues = updatedTask.toMap()
        taskRefByGoalId.updateChildren(taskValues)
    }

    override fun fetchAllTasks(goalId: String): Flow<List<Task>> = callbackFlow {
        val allTasksListener = tasksRef.child(goalId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tasks = mutableListOf<Task>()
                    for (relatedTask in snapshot.children) {
                        val task = relatedTask.getValue(Task::class.java)
                        if (task != null) {
                            tasks.add(task)
                        }
                    }
                    trySend(tasks)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        awaitClose { tasksRef.removeEventListener(allTasksListener) }
    }

    override fun fetchActiveTasks(goalId: String): Flow<List<Task>> = callbackFlow {
        val tasksListener = tasksRef.child(goalId).orderByChild("taskCompleted").equalTo(false)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tasks = mutableListOf<Task>()
                    for (relatedTask in snapshot.children) {
                        val task = relatedTask.getValue(Task::class.java)
                        if (task != null) {
                            tasks.add(task)
                        }
                    }
                    trySend(tasks)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        awaitClose { tasksRef.removeEventListener(tasksListener) }
    }

    override fun fetchCompletedTasks(goalId: String): Flow<List<Task>> = callbackFlow {
        val completedTasksListener =
            tasksRef.child(goalId).orderByChild("taskCompleted").equalTo(true)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val tasks = mutableListOf<Task>()
                        for (relatedTask in snapshot.children) {
                            val task = relatedTask.getValue(Task::class.java)
                            if (task != null) {
                                tasks.add(task)
                            }
                        }
                        trySend(tasks)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        awaitClose { tasksRef.removeEventListener(completedTasksListener) }
    }
}