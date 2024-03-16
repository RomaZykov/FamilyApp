package com.n1.moguchi.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Child(
    val childId: String? = null,
    val parentOwnerId: String? = null,
    var childName: String? = null,
    var imageResourceId: Int? = null,
    var passwordFromParent: Int? = null,
    val onBoardingCompleted: Boolean = false,
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
