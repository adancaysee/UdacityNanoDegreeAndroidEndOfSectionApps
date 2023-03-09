package com.udacity.loadapp

import android.app.Application
import com.udacity.loadapp.repository.DownloadFileRepository
import com.udacity.loadapp.util.createDownloadCompletedNotificationChannel
import com.udacity.loadapp.util.getNotificationManager
import timber.log.Timber

class LoadAppApplication : Application() {

    val downloadFileRepository: DownloadFileRepository = DownloadFileRepository()

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        createDownloadCompletedNotificationChannel(
            getNotificationManager(this),
            getString(R.string.channel_id),
            getString(R.string.channel_name)
        )


    }
}