package com.n1.moguchi.di.modules.goal

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.n1.moguchi.data.impl.GoalRepositoryImpl
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.di.components.ApplicationScope
import com.n1.moguchi.di.modules.ViewModelKey
import com.n1.moguchi.ui.fragment.parent.goal_creation.GoalCreationViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface GoalModule {

    @IntoMap
    @ViewModelKey(GoalCreationViewModel::class)
    @Binds
    fun bindGoalCreationDialogViewModel(bottomSheetViewModel: GoalCreationViewModel): ViewModel

    companion object {

        @ApplicationScope
        @Provides
        fun provideGoalRepositoryImpl(
            database: FirebaseDatabase,
            auth: FirebaseAuth
        ): GoalRepository {
            return GoalRepositoryImpl(database, auth)
        }
    }
}