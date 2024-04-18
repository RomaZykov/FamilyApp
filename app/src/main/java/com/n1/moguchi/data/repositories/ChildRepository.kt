package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.remote.Child
import kotlinx.coroutines.flow.Flow

interface ChildRepository {

    fun fetchChildData(childId: String): Flow<Child?>
}