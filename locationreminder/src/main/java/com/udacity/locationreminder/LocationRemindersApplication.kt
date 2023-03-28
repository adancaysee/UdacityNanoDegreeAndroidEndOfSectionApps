package com.udacity.locationreminder

import android.app.Application
import com.udacity.locationreminder.util.createNotificationChannel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class LocationRemindersApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        setupKoin()
        createNotificationChannel(this)
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@LocationRemindersApplication)
            modules(reminderAppModule)
        }
    }
}