package com.n1.moguchi.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.n1.moguchi.data.local.entities.ChildDbModel

@Dao
interface ChildrenListDao {
    @Query("SELECT * FROM children_table")
    fun getChildrenList(): LiveData<List<ChildDbModel>>
}