package com.n1.moguchi.data.models

data class Task(
    val taskId: String = "",
    var title: String = "",
    var goalOwnerId: String? = null,
    var childOwnerId: String? = null,
    val taskCompleted: Boolean = false,
    var height: Int = 1
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "taskId" to taskId,
            "title" to title,
            "goalOwnerId" to goalOwnerId,
            "childOwnerId" to childOwnerId,
            "taskCompleted" to taskCompleted,
            "height" to height
        )
    }
}