package com.n1.moguchi.data.models

data class Goal(
    var goalId: String? = null,
    var parentOwnerId: String? = null,
    var childOwnerId: String? = null,
    val title: String = "",
    val totalPoints: Int = 0,
    val currentPoints: Int = 0,
    val goalCompleted: Boolean = false
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "goalId" to goalId,
            "parentOwnerId" to parentOwnerId,
            "childOwnerId" to childOwnerId,
            "title" to title,
            "totalPoints" to totalPoints,
            "currentPoints" to currentPoints,
            "goalCompleted" to goalCompleted
        )
    }
}