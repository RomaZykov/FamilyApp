package com.n1.moguchi.di.modules.goal

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.n1.moguchi.data.impl.GoalRepositoryImpl
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.di.components.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
interface GoalModule {

    companion object {

        @ApplicationScope
        @Provides
        fun provideGoalRepositoryImpl(
            database: FirebaseDatabase,
            auth: FirebaseAuth
        ): GoalRepository {
            return GoalRepositoryImpl(database, auth)
        }
    }
}