package com.example.familyapp.domain.usecases

import com.example.familyapp.data.remote.model.Child
import com.example.familyapp.domain.repositories.ChildRepository
import com.example.familyapp.di.modules.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchChildDataUseCase @Inject constructor(
    private val repository: ChildRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    operator fun invoke(id: String): Flow<Child?> =
        repository.fetchChildData(id).flowOn(dispatcher)
}