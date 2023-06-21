package com.n1.moguchi.domain.models.tasks

import com.n1.moguchi.domain.models.goals.Priority

data class Task(
    val taskId: Int,
    val description: String? = null,
    val isTaskCompleted: Boolean = false,
    val height: Int,
    val priority: Priority
)