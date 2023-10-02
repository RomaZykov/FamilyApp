package com.n1.moguchi

import android.app.Application
import com.n1.moguchi.components.AppComponent
import com.n1.moguchi.components.DaggerAppComponent

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