package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.Child

interface ParentRepository {

    suspend fun fetchChildren(parentId: String): Map<String, Child>

    suspend fun getChild(parentId: String, childId: String): Child

    suspend fun setPassword(password: Int, childId: String)

    fun getAndSaveChildToDb(parentId: String, childUser: Child): Child

    fun getAndUpdateChildInDb(parentId: String, childUser: Child): Child

    suspend fun deleteChildProfile(parentId: String, childId: String)
}