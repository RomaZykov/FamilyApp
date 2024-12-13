package com.example.familyapp.di.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.familyapp.BuildConfig
import com.example.familyapp.di.components.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
interface FirebaseModule {

    companion object {
        @ApplicationScope
        @Provides
        fun provideFirebaseAuthInstance(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }

        @Provides
        fun provideFirebaseDatabaseInstance(): FirebaseDatabase {
            return FirebaseDatabase.getInstance(BuildConfig.BASE_URL)
        }
    }
}