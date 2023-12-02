package com.n1.moguchi.components

import android.app.Application
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.data.RepositoryModule
import com.n1.moguchi.modules.ViewModelModule
import com.n1.moguchi.ui.fragments.PasswordFragment
import com.n1.moguchi.ui.fragments.parent.children_creation.AddChildFragment
import com.n1.moguchi.ui.fragments.parent.goal_creation.GoalCreationFragment
import com.n1.moguchi.ui.fragments.parent.ParentHomeFragment
import com.n1.moguchi.ui.fragments.parent.PrimaryBottomSheetFragment
import com.n1.moguchi.ui.fragments.parent.task_creation.TaskCreationFragment
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