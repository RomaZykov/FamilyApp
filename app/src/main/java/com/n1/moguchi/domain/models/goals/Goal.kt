package com.n1.moguchi.domain.models.goals

import com.n1.moguchi.domain.models.tasks.Task

data class Goal(
    val goalId: Int,
    val taskList: List<Task>,
    val description: String? = null,
    val isGoalCompleted: Boolean
)