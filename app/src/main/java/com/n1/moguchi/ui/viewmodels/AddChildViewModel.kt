package com.n1.moguchi.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.data.repositories.ParentRepository
import com.n1.moguchi.data.repositories.TaskRepository
import javax.inject.Inject

class AddChildViewModel @Inject constructor(
    private val parentRepository: ParentRepository,
    private val goalRepository: GoalRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    fun addChild(parentId: String, child: Child) {
        parentRepository.saveChild(parentId, child)
    }
}