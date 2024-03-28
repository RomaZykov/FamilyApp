package com.n1.moguchi.di.modules

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

@Module
interface ApplicationContextModule {
    @Binds
    @AppContext
    fun bindApplicationContext(application: Application): Context
}
