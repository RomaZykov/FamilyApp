package com.n1.moguchi.data.models.remote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val taskId: String = "",
    var title: String = "",
    var goalOwnerId: String? = null,
    var taskCompleted: Boolean = false,
    var onCheck: Boolean = false,
    var height: Int = 1
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