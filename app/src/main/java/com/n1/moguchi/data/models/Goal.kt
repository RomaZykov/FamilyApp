package com.n1.moguchi.data.models

data class Goal(
    var goalId: String? = "",
    var parentOwnerId: String? = "",
    var childOwnerId: String? = "",
    val taskList: List<Task>,
    val title: String? = "",
    val height: Int? = 0,
    val isGoalCompleted: Boolean = false
)