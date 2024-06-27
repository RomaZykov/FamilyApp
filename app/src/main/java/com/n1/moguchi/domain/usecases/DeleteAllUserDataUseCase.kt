package com.n1.moguchi.domain.usecases

import com.n1.moguchi.domain.repositories.ParentRepository
import javax.inject.Inject

class DeleteAllUserDataUseCase @Inject constructor(
    private val repository: ParentRepository
) {
    suspend fun deleteAllUserData(id: String) = repository.deleteAllUserData(id)
}