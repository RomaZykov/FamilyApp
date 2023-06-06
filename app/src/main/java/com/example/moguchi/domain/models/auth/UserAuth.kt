package com.example.moguchi.domain.models.auth

data class UserAuth(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: String
)