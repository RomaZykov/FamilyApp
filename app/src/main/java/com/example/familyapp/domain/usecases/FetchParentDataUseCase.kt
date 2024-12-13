package com.example.familyapp.domain.usecases

import com.example.familyapp.data.remote.model.Parent
import com.example.familyapp.domain.repositories.ParentRepository
import com.example.familyapp.di.modules.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchParentDataUseCase @Inject constructor(
    private val repository: ParentRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    operator fun invoke(id: String): Flow<Parent?> =
        repository.fetchParentData(id).flowOn(dispatcher)
}