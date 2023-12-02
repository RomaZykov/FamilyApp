package com.n1.moguchi.data.models

data class Child(
    val childId: String? = null,
    val parentOwnerId: String? = null,
    var childName: String? = null,
    var imageResourceId: Int? = null,
    var passwordFromParent: Int = 0,
    val childMode: Boolean = false
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "childId" to childId,
            "parentOwnerId" to parentOwnerId,
            "childName" to childName,
            "imageResourceId" to imageResourceId,
            "passwordFromParent" to passwordFromParent,
            "childMode" to childMode
        )
    }
}