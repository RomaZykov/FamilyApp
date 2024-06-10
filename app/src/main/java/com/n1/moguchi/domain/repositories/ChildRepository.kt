package com.n1.moguchi.domain.repositories

import com.n1.moguchi.data.remote.model.Child
import kotlinx.coroutines.flow.Flow

interface ChildRepository {

    fun fetchChildData(childId: String): Flow<Child?>
}