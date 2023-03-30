package com.udacity.project4

import android.app.Application
import com.udacity.project4.geofence.createNotificationChannel
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