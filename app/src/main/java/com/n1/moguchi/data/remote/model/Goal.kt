package com.n1.moguchi.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Use default values for firebase
@Parcelize
data class Goal(
    val goalId: String? = null,
    val parentOwnerId: String? = null,
    val childOwnerId: String? = null,
    val title: String = "",
    val currentPoints: Int = 0,
    val secondaryPoints: Int = 0,
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
            "secondaryPoints" to secondaryPoints,
            "totalPoints" to totalPoints,
            "goalCompleted" to goalCompleted
        )
    }
}