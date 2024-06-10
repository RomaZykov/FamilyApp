package com.n1.moguchi.usecases

import com.n1.moguchi.data.repositories.ParentRepository
import javax.inject.Inject

class DeleteAllUserDataUseCase @Inject constructor(
    private val repository: ParentRepository
) {
    suspend fun deleteAllUserData(id: String) = repository.deleteAllUserData(id)
}