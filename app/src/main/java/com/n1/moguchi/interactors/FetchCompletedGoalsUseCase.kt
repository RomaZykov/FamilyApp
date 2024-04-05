package com.n1.moguchi.interactors

import com.n1.moguchi.data.models.remote.Goal
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.di.modules.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FetchCompletedGoalsUseCase @Inject constructor(
    private val repository: GoalRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    operator fun invoke(goalId: String): Flow<List<Goal>?> =
        repository.fetchCompletedGoals(goalId).flowOn(dispatcher)
}