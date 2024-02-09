package com.n1.moguchi.di.modules.switch_to_user

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.n1.moguchi.data.impl.AppRepositoryImpl
import com.n1.moguchi.data.repositories.AppRepository
import com.n1.moguchi.di.components.ApplicationScope
import com.n1.moguchi.di.modules.ViewModelKey
import com.n1.moguchi.ui.fragment.switch_to_user.SwitchToUserViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface SwitchToUserModule {

    @IntoMap
    @ViewModelKey(SwitchToUserViewModel::class)
    @Binds
    fun bindSwitchToUserViewModel(switchToUserViewModel: SwitchToUserViewModel): ViewModel

    companion object {

        @ApplicationScope
        @Provides
        fun provideAppRepositoryImpl(
            database: FirebaseDatabase,
            auth: FirebaseAuth
        ): AppRepository {
            return AppRepositoryImpl(database, auth)
        }
    }
}