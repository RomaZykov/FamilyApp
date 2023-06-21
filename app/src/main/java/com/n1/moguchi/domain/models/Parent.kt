package com.n1.moguchi.domain.models

import com.n1.moguchi.domain.models.child.Child

data class Parent(
    val uid: String? = null,
    val userName: String,
    val email: String,
    val password: String,
    val role: String,
    val listChild: List<Child>? = null
)