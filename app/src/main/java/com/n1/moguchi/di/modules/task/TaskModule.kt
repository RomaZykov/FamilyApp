package com.n1.moguchi.di.modules.task

import com.google.firebase.database.FirebaseDatabase
import com.n1.moguchi.data.impl.TaskRepositoryImpl
import com.n1.moguchi.data.repositories.TaskRepository
import com.n1.moguchi.di.components.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
interface TaskModule {

    companion object {

        @ApplicationScope
        @Provides
        fun provideTaskRepositoryImpl(
            database: FirebaseDatabase,
        ): TaskRepository {
            return TaskRepositoryImpl(database)
        }
    }
}