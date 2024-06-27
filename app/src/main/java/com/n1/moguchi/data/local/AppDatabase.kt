package com.n1.moguchi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.n1.moguchi.data.local.dao.ChildrenListDao
import com.n1.moguchi.data.local.entities.ChildDbModel


interface AppDataBase {
    fun childrenListDao(): ChildrenListDao

    @Database(entities = [ChildDbModel::class], version = 1, exportSchema = false)
    abstract class Base : RoomDatabase(), AppDataBase
}
