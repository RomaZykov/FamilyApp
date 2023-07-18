package com.n1.moguchi.data.models

data class Goal(
    var goalId: String? = null,
    var parentOwnerId: String? = null,
    var childOwnerId: String? = null,
    val taskList: List<Task>? = null,
    val title: String? = null,
    val height: Int? = null,
    val isGoalCompleted: Boolean = false
)