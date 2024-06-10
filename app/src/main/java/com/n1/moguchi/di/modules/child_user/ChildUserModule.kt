package com.n1.moguchi.di.modules.child_user

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.n1.moguchi.data.impl.ChildRepositoryImpl
import com.n1.moguchi.data.repositories.ChildRepository
import com.n1.moguchi.di.components.ApplicationScope
import com.n1.moguchi.di.modules.ViewModelKey
import com.n1.moguchi.presentation.fragment.child.home.HomeChildViewModel
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