package com

import android.app.Application
import com.udacity.loadapp.R
import com.udacity.loadapp.util.createDownloadCompletedNotificationChannel
import com.udacity.loadapp.util.getNotificationManager

class LoadAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        createDownloadCompletedNotificationChannel(
            getNotificationManager(this),
            getString(R.string.channel_id),
            getString(R.string.channel_name)
        )
    }
}