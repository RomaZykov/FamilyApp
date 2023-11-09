package com.n1.moguchi.data.models


data class Child(
    val childId: String? = null,
    val parentOwnerId: String? = null,
    var childName: String = "",
    var isAvatarSelected: Boolean = false,
    val isChildMode: Boolean = false
)