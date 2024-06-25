package com.n1.moguchi.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "children_table")
data class ChildLocal(
    @PrimaryKey @ColumnInfo(name = "childId") val childId: String,
    @ColumnInfo(name = "parentOwnerId") val parentOwnerId: String,
    @ColumnInfo(name = "childName") val childName: String,
    @ColumnInfo(name = "imageResourceId") val imageResourceId: Int,
    @ColumnInfo(name = "passwordFromParent") val passwordFromParent: Int,
    @ColumnInfo(name = "onBoardingCompleted") val onBoardingCompleted: Boolean
)