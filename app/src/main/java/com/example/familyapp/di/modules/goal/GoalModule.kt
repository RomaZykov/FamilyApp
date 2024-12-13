package com.example.familyapp.di.modules.goal

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.familyapp.data.impl.GoalRepositoryImpl
import com.example.familyapp.domain.repositories.GoalRepository
import com.example.familyapp.di.components.ApplicationScope
import com.example.familyapp.di.modules.ViewModelKey
import com.example.familyapp.ui.fragment.parent.goal_creation.GoalCreationViewModel
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