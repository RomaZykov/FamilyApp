package com.example.familyapp.domain.repositories

import com.example.familyapp.data.remote.model.Child
import com.example.familyapp.data.remote.model.Goal
import com.example.familyapp.data.remote.model.Parent
import com.example.familyapp.data.remote.model.Task
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