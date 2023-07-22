package com.n1.moguchi.components

import android.app.Application
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.modules.RepositoryModule
import com.n1.moguchi.modules.ViewModelModule
import com.n1.moguchi.ui.fragments.AddChildFragment
import com.n1.moguchi.ui.fragments.CommonCreationDialog
import com.n1.moguchi.ui.fragments.HomeFragment
import com.n1.moguchi.ui.fragments.TaskCreationDialog
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(addChildFragment: AddChildFragment)

    fun inject(commonCreationDialog: CommonCreationDialog)

    fun inject(taskCreationDialog: TaskCreationDialog)

    fun inject(homeFragment: HomeFragment)

    fun inject(moguchiBaseApplication: MoguchiBaseApplication)

    @Component.Factory
    interface AppComponentFactory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}