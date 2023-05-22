package com.example.moguchi.api

import com.example.moguchi.data.LoginResponse
import com.example.moguchi.data.UserAuth
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(
        @Body auth: UserAuth
    ): Response<LoginResponse>

    @GET("auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") token: String
    ): Response<LoginResponse>
}