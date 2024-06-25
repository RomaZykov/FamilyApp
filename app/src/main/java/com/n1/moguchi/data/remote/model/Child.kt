package com.n1.moguchi.data.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Use default values for firebase
@Parcelize
data class Child(
    val childId: String? = null,
    val parentOwnerId: String? = null,
    val childName: String = "",
    val imageResourceId: Int? = null,
    val passwordFromParent: Int? = null,
    val onBoardingCompleted: Boolean = false
) : Parcelable {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "childId" to childId,
            "parentOwnerId" to parentOwnerId,
            "childName" to childName,
            "imageResourceId" to imageResourceId,
            "passwordFromParent" to passwordFromParent,
            "onBoardingCompleted" to onBoardingCompleted
        )
    }
}
