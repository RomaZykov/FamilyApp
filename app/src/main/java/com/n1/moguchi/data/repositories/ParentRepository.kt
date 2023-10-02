package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.Child

interface ParentRepository {

    suspend fun getChildren(parentId: String): Map<String, Child>

    suspend fun getChild(parentId: String, childId: String): Child

    fun saveChildrenByName(parentId: String, childrenNamesList: List<String>)

    fun deleteChild(parentId: String, childId: String)
}