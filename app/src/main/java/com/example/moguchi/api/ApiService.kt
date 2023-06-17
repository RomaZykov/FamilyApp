package com.example.moguchi.api

import com.example.moguchi.domain.models.auth.LoginResponse
import com.example.moguchi.domain.models.auth.Parent
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @POST("auth/login")
    fun login(@Body auth: Parent): Call<LoginResponse>

    @GET("auth/refresh")
    fun refreshToken(@Header("Authorization") token: String): Call<LoginResponse>

    @POST("register")
    fun registerUser(@Body user: Parent): Call<Parent>

    @POST
    fun createTask()

    @PUT
    fun updateTask()

    @DELETE
    fun deleteTask()

    @POST
    fun createGoal()

    @PUT
    fun updateGoal()

    @DELETE
    fun deleteGoal()

    @POST
    fun addChild()
}