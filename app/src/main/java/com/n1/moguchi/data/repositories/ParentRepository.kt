package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.Child

interface ParentRepository {

    fun getChildrenList(parentId: String): List<Child>

    fun getChild(parentId: String, childId: String): Child

    fun deleteChild(parentId: String, childId: String)
}