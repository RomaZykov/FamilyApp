package com.n1.moguchi.di.components

import android.app.Application
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.data.RepositoryModule
import com.n1.moguchi.di.modules.ViewModelModule
import com.n1.moguchi.ui.fragment.parent.password.PasswordFragment
import com.n1.moguchi.ui.fragment.parent.children_creation.AddChildFragment
import com.n1.moguchi.ui.fragment.parent.goal_creation.GoalCreationFragment
import com.n1.moguchi.ui.fragment.parent.home.ParentHomeFragment
import com.n1.moguchi.ui.fragment.parent.PrimaryBottomSheetFragment
import com.n1.moguchi.ui.fragment.parent.task_creation.TaskCreationFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(addChildFragment: AddChildFragment)

    fun inject(passwordFragment: PasswordFragment)

    fun inject(goalCreationFragment: GoalCreationFragment)

    fun inject(bottomSheetFragment: PrimaryBottomSheetFragment)

    fun inject(taskCreationFragment: TaskCreationFragment)

    fun inject(parentHomeFragment: ParentHomeFragment)

    fun inject(moguchiBaseApplication: MoguchiBaseApplication)

    @Component.Factory
    interface AppComponentFactory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}