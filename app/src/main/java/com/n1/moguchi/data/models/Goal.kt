package com.n1.moguchi.data.models

data class Goal(
    var goalId: String,
    val taskList: List<Task>,
    val description: String? = null,
    val height: Int,
    val isGoalCompleted: Boolean = false
)