package com.n1.moguchi.components

import android.app.Application
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.modules.RepositoryModule
import com.n1.moguchi.modules.ViewModelModule
import com.n1.moguchi.ui.fragments.parent.AddChildFragment
import com.n1.moguchi.ui.fragments.parent.GoalCreationFragment
import com.n1.moguchi.ui.fragments.parent.HomeFragment
import com.n1.moguchi.ui.fragments.parent.PrimaryBottomSheetFragment
import com.n1.moguchi.ui.fragments.parent.TaskCreationFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(addChildFragment: AddChildFragment)

    fun inject(goalCreationFragment: GoalCreationFragment)

    fun inject(bottomSheetFragment: PrimaryBottomSheetFragment)

    fun inject(taskCreationFragment: TaskCreationFragment)

    fun inject(homeFragment: HomeFragment)

    fun inject(moguchiBaseApplication: MoguchiBaseApplication)

    @Component.Factory
    interface AppComponentFactory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}