package com.n1.moguchi.domain.models


data class Child(
    val uid: String? = null,
    val childName: String,
    val years: Int,
    val parentOwnerId: String,
)