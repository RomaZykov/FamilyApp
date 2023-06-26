package com.n1.moguchi.domain.models.goals

import com.n1.moguchi.domain.models.tasks.Task

data class Goal(
    var goalId: String,
    val taskList: List<Task>,
    val description: String? = null,
    val isGoalCompleted: Boolean = false
)