package com.n1.moguchi.ui.viewmodels

import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.data.repositories.ParentRepository
import com.n1.moguchi.data.repositories.TaskRepository
import javax.inject.Inject

class AddChildViewModel @Inject constructor(
    private val parentRepository: ParentRepository,
    private val goalRepository: GoalRepository,
    private val taskRepository: TaskRepository
) : BaseViewModel() {

    fun saveChildrenList(parentId: String, childrenNamesList: List<String>) {
        parentRepository.saveChildrenByName(parentId, childrenNamesList)
    }
}