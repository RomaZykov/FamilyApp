package com.example.familyapp.di.modules.child_user

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.example.familyapp.data.impl.ChildRepositoryImpl
import com.example.familyapp.domain.repositories.ChildRepository
import com.example.familyapp.di.components.ApplicationScope
import com.example.familyapp.di.modules.ViewModelKey
import com.example.familyapp.ui.fragment.child.home.HomeChildViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface ChildUserModule {

    @IntoMap
    @ViewModelKey(HomeChildViewModel::class)
    @Binds
    fun bindHomeChildViewModel(homeChildViewModel: HomeChildViewModel): ViewModel

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