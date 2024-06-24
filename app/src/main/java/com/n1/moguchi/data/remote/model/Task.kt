package com.n1.moguchi.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Use default values for firebase
@Parcelize
data class Task(
    val taskId: String = "",
    val title: String = "",
    val goalOwnerId: String? = null,
    val taskCompleted: Boolean = false,
    val onCheck: Boolean = false,
    val height: Int = 1
) : Parcelable {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "taskId" to taskId,
            "title" to title,
            "goalOwnerId" to goalOwnerId,
            "taskCompleted" to taskCompleted,
            "onCheck" to onCheck,
            "height" to height
        )
    }
}