package com.udacity.politicalpreparedness

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class PoliticalPreparednessApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        configureKoin()
    }

    private fun configureKoin() {
        startKoin {
            androidContext(this@PoliticalPreparednessApplication)
            modules(appModule)
        }
    }
}