package com.n1.moguchi.modules

import androidx.lifecycle.ViewModel
import com.n1.moguchi.helpers.ViewModelKey
import com.n1.moguchi.ui.fragments.parent.children_creation.AddChildViewModel
import com.n1.moguchi.ui.viewmodels.HomeViewModel
import com.n1.moguchi.ui.fragments.parent.task_creation.TaskCreationViewModel
import com.n1.moguchi.ui.activity.MainActivityViewModel
import com.n1.moguchi.ui.fragments.parent.goal_creation.GoalCreationViewModel
import com.n1.moguchi.ui.viewmodels.PasswordViewModel
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
    @ViewModelKey(PasswordViewModel::class)
    @Binds
    fun bindPasswordViewModel(passwordViewModel: PasswordViewModel): ViewModel

    @IntoMap
    @ViewModelKey(GoalCreationViewModel::class)
    @Binds
    fun bindGoalCreationDialogViewModel(bottomSheetViewModel: GoalCreationViewModel): ViewModel

    @IntoMap
    @ViewModelKey(TaskCreationViewModel::class)
    @Binds
    fun bindTaskViewModel(taskCreationViewModel: TaskCreationViewModel): ViewModel

    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    @Binds
    fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    @Binds
    fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel
}