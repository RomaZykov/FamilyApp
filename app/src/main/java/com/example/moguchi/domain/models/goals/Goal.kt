package com.example.moguchi.domain.models.goals

import com.example.moguchi.domain.models.tasks.Task

data class Goal(
    val goalId: Int,
    val taskList: List<Task>,
    val description: String? = null,
    val isGoalCompleted: Boolean
)