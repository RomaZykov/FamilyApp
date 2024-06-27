package com.n1.moguchi.domain.usecases

import com.n1.moguchi.data.remote.model.Parent
import com.n1.moguchi.domain.repositories.ParentRepository
import com.n1.moguchi.di.modules.IoDispatcher
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