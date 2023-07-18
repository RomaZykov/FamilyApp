package com.n1.moguchi.components

import android.app.Application
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.modules.RepositoryModule
import com.n1.moguchi.modules.ViewModelModule
import com.n1.moguchi.ui.fragments.AddChildFragment
import com.n1.moguchi.ui.fragments.GoalAndTaskCreationDialogFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(addChildFragment: AddChildFragment)

    fun inject(goalAndTaskCreationDialogFragment: GoalAndTaskCreationDialogFragment)

    fun inject(moguchiBaseApplication: MoguchiBaseApplication)

    @Component.Factory
    interface AppComponentFactory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}