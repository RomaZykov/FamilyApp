package com.n1.moguchi.data.models

data class Task(
    val taskId: String = "",
    val title: String = "",
    val isTaskCompleted: Boolean = false,
    val height: Int = 0
)