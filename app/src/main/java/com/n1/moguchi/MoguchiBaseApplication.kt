package com.n1.moguchi

import android.app.Application
import com.n1.moguchi.di.components.AppComponent
import com.n1.moguchi.di.components.DaggerAppComponent

class MoguchiBaseApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory()
            .create(this)
    }

    override fun onCreate() {
        appComponent.inject(this)
        super.onCreate()
    }
}