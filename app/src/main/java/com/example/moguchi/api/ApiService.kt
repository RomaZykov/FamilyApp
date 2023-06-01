package com.example.moguchi.api

import com.example.moguchi.domain.models.auth.LoginResponse
import com.example.moguchi.domain.models.auth.UserAuth
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun login(@Body auth: UserAuth): Call<LoginResponse>

    @GET("auth/refresh")
    fun refreshToken(@Header("Authorization") token: String): Call<LoginResponse>

    @POST("register")
    fun registerUser(@Body user: UserAuth): Call<UserAuth>
}