package com.n1.moguchi.di.modules.child_user

import com.google.firebase.database.FirebaseDatabase
import com.n1.moguchi.data.impl.ChildRepositoryImpl
import com.n1.moguchi.data.repositories.ChildRepository
import com.n1.moguchi.di.components.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
interface ChildUserModule {

    companion object {

        @ApplicationScope
        @Provides
        fun provideChildRepositoryImpl(
            database: FirebaseDatabase,
        ): ChildRepository {
            return ChildRepositoryImpl(database)
        }
    }
}