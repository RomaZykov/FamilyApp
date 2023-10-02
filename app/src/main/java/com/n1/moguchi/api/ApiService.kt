package com.n1.moguchi.api

import com.n1.moguchi.data.models.Parent
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

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