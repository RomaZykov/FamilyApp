package com.example.familyapp.di.modules.parent_user

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.example.familyapp.data.impl.ParentRepositoryImpl
import com.example.familyapp.domain.repositories.ParentRepository
import com.example.familyapp.di.components.ApplicationScope
import com.example.familyapp.di.modules.ViewModelKey
import com.example.familyapp.ui.fragment.parent.child_creation.ChildCreationViewModel
import com.example.familyapp.ui.fragment.parent.home.HomeParentViewModel
import com.example.familyapp.ui.fragment.parent.profile.ProfileParentViewModel
import com.example.familyapp.ui.fragment.parent.profile.related_bottom_sheet.ProfileBottomSheetViewModel
import com.example.familyapp.ui.fragment.parent.profile.related_bottom_sheet.edit_profile.EditParentViewModel
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
    @ViewModelKey(ProfileParentViewModel::class)
    @Binds
    fun bindProfileParentViewModel(profileParentViewModel: ProfileParentViewModel): ViewModel

    @IntoMap
    @ViewModelKey(EditParentViewModel::class)
    @Binds
    fun bindEditParentViewModel(editParentViewModel: EditParentViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ProfileBottomSheetViewModel::class)
    @Binds
    fun bindProfileBottomSheetViewModel(profileBottomSheetViewModel: ProfileBottomSheetViewModel): ViewModel

    companion object {

        @ApplicationScope
        @Provides
        fun provideParentRepositoryImpl(
            database: FirebaseDatabase
        ): ParentRepository {
            return ParentRepositoryImpl(database)
        }
    }
}