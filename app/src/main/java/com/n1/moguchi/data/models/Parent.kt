package com.n1.moguchi.data.models

data class Parent(
    val uid: String? = null,
    val parentName: String = "",
    val email: String,
    val childrenList: List<Child>? = null,
    var passwordForChild: Int? = null,
    val isParentMode: Boolean = true,
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "parentName" to parentName,
            "email" to email,
            "childrenList" to childrenList,
            "password" to passwordForChild,
            "isParentMode" to isParentMode
        )
    }
}