package com.udacity.asteroidradar

import android.app.Application
import com.udacity.asteroidradar.repository.AppContainer
import com.udacity.asteroidradar.repository.DefaultAppContainer
import timber.log.Timber

class AsteroidRadarApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer(this)
        Timber.plant(Timber.DebugTree())

    }
}