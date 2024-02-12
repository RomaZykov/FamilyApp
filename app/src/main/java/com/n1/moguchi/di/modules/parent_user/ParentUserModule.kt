package com.n1.moguchi.di.modules.parent_user

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.n1.moguchi.data.impl.ParentRepositoryImpl
import com.n1.moguchi.data.repositories.ParentRepository
import com.n1.moguchi.di.components.ApplicationScope
import com.n1.moguchi.di.modules.ViewModelKey
import com.n1.moguchi.ui.fragment.parent.child_creation.ChildCreationViewModel
import com.n1.moguchi.ui.fragment.parent.home.HomeParentViewModel
import com.n1.moguchi.ui.fragment.password.PasswordViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface ParentUserModule {

    @IntoMap
    @ViewModelKey(HomeParentViewModel::class)
    @Binds
    fun bindHomeParentViewModel(homeParentViewModel: HomeParentViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ChildCreationViewModel::class)
    @Binds
    fun bindChildCreationViewModel(childCreationViewModel: ChildCreationViewModel): ViewModel

    @IntoMap
    @ViewModelKey(PasswordViewModel::class)
    @Binds
    fun bindPasswordViewModel(passwordViewModel: PasswordViewModel): ViewModel

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