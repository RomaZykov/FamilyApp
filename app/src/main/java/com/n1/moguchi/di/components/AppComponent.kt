package com.n1.moguchi.di.components

import android.app.Application
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.di.modules.FirebaseModule
import com.n1.moguchi.di.modules.ViewModelModule
import com.n1.moguchi.di.modules.child_user.ChildUserModule
import com.n1.moguchi.di.modules.goal.GoalModule
import com.n1.moguchi.di.modules.parent_user.ParentUserModule
import com.n1.moguchi.di.modules.task.TaskModule
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.fragment.parent.password.PasswordFragment
import com.n1.moguchi.ui.fragment.parent.children_creation.ChildCreationFragment
import com.n1.moguchi.ui.fragment.parent.goal_creation.GoalCreationFragment
import com.n1.moguchi.ui.fragment.parent.home.ParentHomeFragment
import com.n1.moguchi.ui.fragment.parent.task_creation.TaskCreationFragment
import com.n1.moguchi.ui.fragment.tasks.TasksFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [GoalModule::class, TaskModule::class, ChildUserModule::class, ParentUserModule::class, FirebaseModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(childCreationFragment: ChildCreationFragment)

    fun inject(tasksFragment: TasksFragment)

    fun inject(passwordFragment: PasswordFragment)

    fun inject(goalCreationFragment: GoalCreationFragment)

    fun inject(taskCreationFragment: TaskCreationFragment)

    fun inject(parentHomeFragment: ParentHomeFragment)

    fun inject(moguchiBaseApplication: MoguchiBaseApplication)

    @Component.Factory
    interface AppComponentFactory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}