package com.n1.moguchi.di.modules

import androidx.lifecycle.ViewModel
import com.n1.moguchi.ui.fragment.parent.children_creation.AddChildViewModel
import com.n1.moguchi.ui.fragment.parent.home.ParentHomeViewModel
import com.n1.moguchi.ui.fragment.parent.task_creation.TaskCreationViewModel
import com.n1.moguchi.ui.activity.MainActivityViewModel
import com.n1.moguchi.ui.fragment.parent.goal_creation.GoalCreationViewModel
import com.n1.moguchi.ui.fragment.parent.password.PasswordViewModel
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
    @ViewModelKey(ParentHomeViewModel::class)
    @Binds
    fun bindHomeViewModel(parentHomeViewModel: ParentHomeViewModel): ViewModel
}