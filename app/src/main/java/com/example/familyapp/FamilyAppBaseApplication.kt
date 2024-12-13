package com.example.familyapp

import android.app.Application
import com.example.familyapp.di.components.AppComponent
import com.example.familyapp.di.components.DaggerAppComponent

class FamilyAppBaseApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory()
            .create(this)
    }

    override fun onCreate() {
        appComponent.inject(this)
        super.onCreate()
    }
}