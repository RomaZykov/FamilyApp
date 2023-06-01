package com.example.moguchi.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    fun registerUser(userEntity: UserEntity)

    @Query("SELECT * FROM users WHERE id=:userId")
    fun getUserById(userId: Long)

    @Query("DELETE FROM users WHERE id=:userId")
    fun deleteUser(userId: Long)
}