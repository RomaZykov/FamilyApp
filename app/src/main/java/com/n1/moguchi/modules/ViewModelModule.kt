package com.n1.moguchi.modules

import androidx.lifecycle.ViewModel
import com.n1.moguchi.helpers.ViewModelKey
import com.n1.moguchi.ui.viewmodels.AddChildViewModel
import com.n1.moguchi.ui.viewmodels.AuthViewModel
import com.n1.moguchi.ui.viewmodels.BottomSheetViewModel
import com.n1.moguchi.ui.viewmodels.HomeViewModel
import com.n1.moguchi.ui.viewmodels.TaskViewModel
import com.n1.moguchi.ui.viewmodels.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(AddChildViewModel::class)
    @Binds
    fun bindAddChildViewModel(addChildViewModel: AddChildViewModel): ViewModel

    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    @Binds
    fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel

    @IntoMap
    @ViewModelKey(BottomSheetViewModel::class)
    @Binds
    fun bindGoalCreationDialogViewModel(bottomSheetViewModel: BottomSheetViewModel): ViewModel

    @IntoMap
    @ViewModelKey(TaskViewModel::class)
    @Binds
    fun bindTaskViewModel(taskViewModel: TaskViewModel): ViewModel

    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    @Binds
    fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    @Binds
    fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel
}