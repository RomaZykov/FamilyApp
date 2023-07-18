package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.Child

interface ParentRepository {

    suspend fun getChildrenList(parentId: String): List<Child>

    suspend fun getChild(parentId: String, childId: String): Child

    fun saveChild(parentId: String, child: Child)

    fun deleteChild(parentId: String, childId: String)
}