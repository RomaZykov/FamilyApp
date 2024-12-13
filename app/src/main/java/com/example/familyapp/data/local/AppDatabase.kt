package com.example.familyapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.familyapp.data.local.dao.ChildrenListDao
import com.example.familyapp.data.local.entities.ChildDbModel


interface AppDataBase {
    fun childrenListDao(): ChildrenListDao

    @Database(entities = [ChildDbModel::class], version = 1, exportSchema = false)
    abstract class Base : RoomDatabase(), AppDataBase
}
