package com.example.familyapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "children_table")
data class ChildDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int
)
