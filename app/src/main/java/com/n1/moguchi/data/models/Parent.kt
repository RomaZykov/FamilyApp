package com.n1.moguchi.data.models

data class Parent(
    val uid: String? = null,
    val parentName: String? = null,
    val email: String? = null,
    val childrenList: List<Child>? = null,
    val childrenPasswordsMap: MutableMap<Int, Child>? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "parentName" to parentName,
            "email" to email,
            "childrenList" to childrenList,
            "childrenPasswordsMap" to childrenPasswordsMap
        )
    }
}