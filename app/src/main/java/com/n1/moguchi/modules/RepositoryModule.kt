package com.n1.moguchi.modules

import com.n1.moguchi.data.implementations.GoalRepositoryImpl
import com.n1.moguchi.data.implementations.ParentRepositoryImpl
import com.n1.moguchi.data.implementations.TaskRepositoryImpl
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.data.repositories.ParentRepository
import com.n1.moguchi.data.repositories.TaskRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindParentRepository(impl: ParentRepositoryImpl): ParentRepository

    @Binds
    abstract fun bindGoalRepository(impl: GoalRepositoryImpl): GoalRepository

    @Binds
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository
}