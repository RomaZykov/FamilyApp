package com.example.familyapp.di.components

import android.app.Application
import com.example.familyapp.FamilyAppBaseApplication
import com.example.familyapp.di.modules.ApplicationContextModule
import com.example.familyapp.di.modules.DispatcherModule
import com.example.familyapp.di.modules.FirebaseModule
import com.example.familyapp.di.modules.ImageModule
import com.example.familyapp.di.modules.ViewModelModule
import com.example.familyapp.di.modules.child_user.ChildUserModule
import com.example.familyapp.di.modules.goal.GoalModule
import com.example.familyapp.di.modules.parent_user.ParentUserModule
import com.example.familyapp.di.modules.task.TaskModule
import com.example.familyapp.ui.activity.MainActivity
import com.example.familyapp.ui.fragment.AfterOnBoardingFragment
import com.example.familyapp.ui.fragment.child.home.HomeChildFragment
import com.example.familyapp.ui.fragment.parent.PrimaryContainerBottomSheetFragment
import com.example.familyapp.ui.fragment.parent.child_creation.ChildCreationFragment
import com.example.familyapp.ui.fragment.parent.goal_creation.GoalCreationFragment
import com.example.familyapp.ui.fragment.parent.home.HomeParentFragment
import com.example.familyapp.ui.fragment.parent.profile.ProfileParentFragment
import com.example.familyapp.ui.fragment.parent.profile.related_bottom_sheet.ProfileContainerBottomSheetFragment
import com.example.familyapp.ui.fragment.parent.profile.related_bottom_sheet.edit_profile.EditParentProfileFragment
import com.example.familyapp.ui.fragment.parent.registration.RegistrationFragment
import com.example.familyapp.ui.fragment.parent.task_creation.TaskCreationFragment
import com.example.familyapp.ui.fragment.switch_to_user.SwitchToChildBottomSheetFragment
import com.example.familyapp.ui.fragment.switch_to_user.SwitchToParentBottomSheetFragment
import com.example.familyapp.ui.fragment.tasks.TasksFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        GoalModule::class,
        TaskModule::class,
        ChildUserModule::class,
        ParentUserModule::class,
        FirebaseModule::class,
        DispatcherModule::class,
        ViewModelModule::class,
        ApplicationContextModule::class,
        ImageModule::class]
)
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(registrationFragment: RegistrationFragment)

    fun inject(afterOnBoardingFragment: AfterOnBoardingFragment)

    fun inject(primaryContainerBottomSheetFragment: PrimaryContainerBottomSheetFragment)

    fun inject(childCreationFragment: ChildCreationFragment)

    fun inject(switchToChildBottomSheetFragment: SwitchToChildBottomSheetFragment)

    fun inject(switchToParentBottomSheetFragment: SwitchToParentBottomSheetFragment)

    fun inject(tasksFragment: TasksFragment)

    fun inject(goalCreationFragment: GoalCreationFragment)

    fun inject(taskCreationFragment: TaskCreationFragment)

    fun inject(homeParentFragment: HomeParentFragment)

    fun inject(homeChildFragment: HomeChildFragment)

    fun inject(profileParentFragment: ProfileParentFragment)

    fun inject(editParentProfileFragment: EditParentProfileFragment)

    fun inject(profileContainerBottomSheetFragment: ProfileContainerBottomSheetFragment)

    fun inject(familyAppBaseApplication: FamilyAppBaseApplication)

    @Component.Factory
    interface AppComponentFactory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}