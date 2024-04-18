package com.n1.moguchi.data.repositories

import com.n1.moguchi.data.models.remote.Child
import com.n1.moguchi.data.models.remote.Goal
import com.n1.moguchi.data.models.remote.Parent
import com.n1.moguchi.data.models.remote.Task
import kotlinx.coroutines.flow.Flow

interface ParentRepository {

    fun fetchParentData(parentId: String): Flow<Parent?>

    suspend fun fetchChildren(parentId: String): Map<String, Child>

    fun returnCreatedChild(parentId: String, childUser: Child): Child

    fun getAndUpdateChildInDb(parentId: String, childUser: Child): Child

    suspend fun saveChildrenToDb(
        children: List<Child>,
        goals: List<Goal>?,
        tasks: List<Task>?
    )

    suspend fun deleteChildProfile(childId: String)

    suspend fun deleteAllUserData(parentId: String)

    fun checkPassword(password: Int, childId: String): Flow<Boolean>
}