package com.n1.moguchi.data.models

data class Child(
    val childId: String? = null,
    val parentOwnerId: String? = null,
    var childName: String? = "",
    var imageResourceId: Int? = null,
    var passwordFromParent: Int? = null,
    val isChildMode: Boolean = false
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "childId" to childId,
            "parentOwnerId" to parentOwnerId,
            "childName" to childName,
            "imageResourceId" to imageResourceId,
            "password" to passwordFromParent,
            "isChildMode" to isChildMode
        )
    }
}