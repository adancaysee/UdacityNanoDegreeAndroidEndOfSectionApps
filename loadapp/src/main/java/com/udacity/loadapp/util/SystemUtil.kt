package com.udacity.loadapp.util

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Context

fun getDownloadManager(context: Context) =
    context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

fun getNotificationManager(context: Context) =
    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager