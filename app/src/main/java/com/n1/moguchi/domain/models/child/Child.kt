package com.n1.moguchi.domain.models.child


data class Child(
    val uid: String? = null,
    val childName: String,
    val years: Int,
    val parentOwnerId: String,
)