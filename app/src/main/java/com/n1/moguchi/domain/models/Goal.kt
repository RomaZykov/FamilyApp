package com.n1.moguchi.domain.models

data class Goal(
    var goalId: String,
    val taskList: List<Task>,
    val description: String? = null,
    val isGoalCompleted: Boolean = false
)