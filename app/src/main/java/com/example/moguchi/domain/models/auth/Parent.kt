package com.example.moguchi.domain.models.auth

import com.example.moguchi.domain.models.child.Child

data class Parent(
    val uid: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: String,
    val listChild: List<Child>? = null
)