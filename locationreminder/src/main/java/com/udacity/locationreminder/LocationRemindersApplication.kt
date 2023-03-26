package com.udacity.locationreminder

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class LocationRemindersApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@LocationRemindersApplication)
            modules(reminderAppModule)
        }
    }
}