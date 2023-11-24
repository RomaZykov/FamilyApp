package com.n1.moguchi.data.models

data class Task(
    val taskId: String = "",
    val title: String = "",
    var goalOwnerId: String? = null,
    var childOwnerId: String? = null,
    val isTaskCompleted: Boolean = false,
    val height: Int = 0
)