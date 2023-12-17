package com.n1.moguchi.data.models

data class Goal(
    var goalId: String? = null,
    var parentOwnerId: String? = null,
    var childOwnerId: String? = null,
    val taskList: List<Task>? = null,
    val title: String = "",
    val height: Int = 0,
    val isGoalCompleted: Boolean = false
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "goalId" to goalId,
            "parentOwnerId" to parentOwnerId,
            "childOwnerId" to childOwnerId,
            "taskList" to taskList,
            "title" to title,
            "height" to height,
            "isGoalCompleted" to isGoalCompleted
        )
    }
}