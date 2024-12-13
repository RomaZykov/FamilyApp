package com.example.familyapp.di.modules.task

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.example.familyapp.data.impl.TaskRepositoryImpl
import com.example.familyapp.domain.repositories.TaskRepository
import com.example.familyapp.di.components.ApplicationScope
import com.example.familyapp.di.modules.ViewModelKey
import com.example.familyapp.ui.fragment.parent.task_creation.TaskCreationViewModel
import com.example.familyapp.ui.fragment.tasks.TasksViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface TaskModule {

    @IntoMap
    @ViewModelKey(TasksViewModel::class)
    @Binds
    fun bindTasksViewModel(tasksViewModel: TasksViewModel): ViewModel

    @IntoMap
    @ViewModelKey(TaskCreationViewModel::class)
    @Binds
    fun bindTaskCreationViewModel(taskCreationViewModel: TaskCreationViewModel): ViewModel

    companion object {

        @ApplicationScope
        @Provides
        fun provideTaskRepositoryImpl(
            database: FirebaseDatabase,
        ): TaskRepository {
            return TaskRepositoryImpl(database)
        }
    }
}