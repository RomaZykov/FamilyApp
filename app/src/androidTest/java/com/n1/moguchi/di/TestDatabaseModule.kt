package com.n1.moguchi.di

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides

@Module
interface TestDatabaseModule {

    companion object {
        @Provides
        fun provideDatabaseInstance(): FirebaseDatabase {
            return FirebaseDatabase.getInstance()
        }
    }
}