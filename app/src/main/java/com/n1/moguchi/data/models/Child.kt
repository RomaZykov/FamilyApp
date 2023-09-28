package com.n1.moguchi.data.models


data class Child(
    val childId: String? = null,
    val parentOwnerId: String? = "",
    val childName: String = "",
    val isChildMode: Boolean = false
)