package com.n1.moguchi.data.models

data class Parent(
    val uid: String? = null,
    val userName: () -> String,
    val email: String,
    val role: String? = "",
    val childrenList: List<Child>? = null
)