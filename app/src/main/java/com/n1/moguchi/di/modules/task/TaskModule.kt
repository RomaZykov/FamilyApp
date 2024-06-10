package com.n1.moguchi.di.modules.task

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.n1.moguchi.data.impl.TaskRepositoryImpl
import com.n1.moguchi.domain.repositories.TaskRepository
import com.n1.moguchi.di.components.ApplicationScope
import com.n1.moguchi.di.modules.ViewModelKey
import com.n1.moguchi.presentation.fragment.parent.task_creation.TaskCreationViewModel
import com.n1.moguchi.presentation.fragment.tasks.TasksViewModel
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