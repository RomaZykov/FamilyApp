package com.example.familyapp.domain.usecases

import com.example.familyapp.domain.repositories.ParentRepository
import javax.inject.Inject

class DeleteAllUserDataUseCase @Inject constructor(
    private val repository: ParentRepository
) {
    suspend fun deleteAllUserData(id: String) = repository.deleteAllUserData(id)
}