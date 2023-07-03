package com.n1.moguchi.domain.models

data class Task(
    val taskId: String,
    val description: String? = null,
    val isTaskCompleted: Boolean = false,
    val height: Int,
    val priority: Priority
)