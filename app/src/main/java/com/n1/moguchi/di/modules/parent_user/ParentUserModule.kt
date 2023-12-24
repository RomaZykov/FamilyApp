package com.n1.moguchi.di.modules.parent_user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.n1.moguchi.data.impl.ParentRepositoryImpl
import com.n1.moguchi.data.repositories.ParentRepository
import com.n1.moguchi.di.components.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
interface ParentUserModule {

    companion object {

        @ApplicationScope
        @Provides
        fun provideParentRepositoryImpl(
            database: FirebaseDatabase,
            auth: FirebaseAuth
        ): ParentRepository {
            return ParentRepositoryImpl(database, auth)
        }
    }
}