package com.n1.moguchi.di.modules.switch_to_user

import androidx.lifecycle.ViewModel
import com.n1.moguchi.data.impl.AppSettingsRepositoryImpl
import com.n1.moguchi.data.repositories.AppSettingsRepository
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
        fun provideAppSettingsRepositoryImpl(): AppSettingsRepository {
            return AppSettingsRepositoryImpl()
        }
    }
}