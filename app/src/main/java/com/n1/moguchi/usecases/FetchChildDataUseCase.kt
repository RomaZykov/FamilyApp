package com.n1.moguchi.usecases

import com.n1.moguchi.data.remote.model.Child
import com.n1.moguchi.data.repositories.ChildRepository
import com.n1.moguchi.di.modules.IoDispatcher
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