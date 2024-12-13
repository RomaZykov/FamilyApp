package com.example.familyapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.familyapp.data.local.entities.ChildDbModel

@Dao
interface ChildrenListDao {
    @Query("SELECT * FROM children_table")
    fun getChildrenList(): LiveData<List<ChildDbModel>>
}