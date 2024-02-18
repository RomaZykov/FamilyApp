package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.models.Parent
import kotlinx.coroutines.flow.Flow

interface ParentRepository {

    fun fetchParentData(parentId: String): Flow<Parent?>

    suspend fun fetchChildren(parentId: String): Map<String, Child>

    fun getAndSaveChildToDb(parentId: String, childUser: Child): Child

    fun getAndUpdateChildInDb(parentId: String, childUser: Child): Child

    suspend fun deleteChildProfile(childId: String)

    suspend fun deleteAllUserData(parentId: String)

    suspend fun setPassword(password: Int, childId: String)

    fun checkPassword(password: Int, childId: String): Flow<Boolean>
}