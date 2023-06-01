package com.example.moguchi.domain.models.auth

import com.google.gson.annotations.SerializedName

data class UserAuth(
    @SerializedName("first_name")
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val role: String
)