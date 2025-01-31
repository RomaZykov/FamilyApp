package com.example.familyapp.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.familyapp.data.impl.AppRepositoryImpl
import com.example.familyapp.domain.repositories.AppRepository
import com.example.familyapp.di.components.ApplicationScope
import com.example.familyapp.ui.activity.MainActivityViewModel
import com.example.familyapp.ui.fragment.AfterOnBoardingViewModel
import com.example.familyapp.ui.fragment.parent.PrimaryContainerViewModel
import com.example.familyapp.ui.fragment.parent.registration.RegistrationViewModel
import com.example.familyapp.ui.fragment.switch_to_user.SwitchToUserViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

private const val CURRENT_PROFILE_MODE = "current_profile_mode"

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    @Binds
    fun bindRegistrationViewModel(registrationViewModel: RegistrationViewModel): ViewModel

    @IntoMap
    @ViewModelKey(SwitchToUserViewModel::class)
    @Binds
    fun bindSwitchToUserViewModel(switchToUserViewModel: SwitchToUserViewModel): ViewModel

    @IntoMap
    @ViewModelKey(PrimaryContainerViewModel::class)
    @Binds
    fun bindPrimaryContainerViewModel(primaryContainerViewModel: PrimaryContainerViewModel): ViewModel

    @IntoMap
    @ViewModelKey(AfterOnBoardingViewModel::class)
    @Binds
    fun bindAfterOnBoardingViewModel(afterOnBoardingViewModel: AfterOnBoardingViewModel): ViewModel

    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    @Binds
    fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

    companion object {
        @ApplicationScope
        @Provides
        fun provideAppRepositoryImpl(
            database: FirebaseDatabase,
            auth: FirebaseAuth,
            dataStore: DataStore<Preferences>
        ): AppRepository {
            return AppRepositoryImpl(database, dataStore, auth)
        }

        @Provides
        @ApplicationScope
        fun providePreferencesDataStore(@AppContext context: Context): DataStore<Preferences> =
            PreferenceDataStoreFactory.create(
                produceFile = {
                    context.preferencesDataStoreFile(CURRENT_PROFILE_MODE)
                }
            )
    }
}