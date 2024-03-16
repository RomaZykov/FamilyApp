package com.n1.moguchi.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Goal(
    var goalId: String? = null,
    var parentOwnerId: String? = null,
    var childOwnerId: String? = null,
    val title: String = "",
    val currentPoints: Int = 0,
    val totalPoints: Int = 0,
    val goalCompleted: Boolean = false
) : Parcelable {

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "goalId" to goalId,
            "parentOwnerId" to parentOwnerId,
            "childOwnerId" to childOwnerId,
            "title" to title,
            "currentPoints" to currentPoints,
            "totalPoints" to totalPoints,
            "goalCompleted" to goalCompleted
        )
    }
}