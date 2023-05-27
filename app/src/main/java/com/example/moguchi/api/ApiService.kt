package com.example.moguchi.api

import com.example.moguchi.domain.models.LoginResponse
import com.example.moguchi.domain.models.RegisterResponse
import com.example.moguchi.domain.models.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body auth: User): Response<LoginResponse>

    @GET("auth/refresh")
    suspend fun refreshToken(@Header("Authorization") token: String): Response<LoginResponse>

    @POST("register")
    fun registerUser(@Body user: User): Call<RegisterResponse>
}