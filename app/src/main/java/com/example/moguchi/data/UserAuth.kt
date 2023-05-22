package com.example.moguchi.data

import com.google.gson.annotations.SerializedName

data class UserAuth(
    @SerializedName("email_address")
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val role: String
)