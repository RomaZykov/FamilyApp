package com.example.familyapp.domain.repositories

import com.example.familyapp.data.remote.model.Child
import kotlinx.coroutines.flow.Flow

interface ChildRepository {

    fun fetchChildData(childId: String): Flow<Child?>
}