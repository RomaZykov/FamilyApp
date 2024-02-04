package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.Child
import kotlinx.coroutines.flow.Flow

interface ChildRepository {

    suspend fun getChild(childId: String): Flow<Child>
}