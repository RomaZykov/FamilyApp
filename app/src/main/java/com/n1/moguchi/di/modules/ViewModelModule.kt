package com.n1.moguchi.di.modules

import androidx.lifecycle.ViewModel
import com.n1.moguchi.ui.activity.MainActivityViewModel
import com.n1.moguchi.ui.fragment.AfterOnBoardingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(AfterOnBoardingViewModel::class)
    @Binds
    fun bindAfterOnBoardingViewModel(afterOnBoardingViewModel: AfterOnBoardingViewModel): ViewModel

    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    @Binds
    fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel
}