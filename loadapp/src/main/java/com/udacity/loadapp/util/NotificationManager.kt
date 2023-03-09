package com.udacity.loadapp.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.udacity.loadapp.R


private const val NOTIFICATION_ID = 0

fun NotificationManager.sendDownloadCompletedNotification(
    context: Context,
    title: String,
    body: String,
    args: Bundle,
) {

    val pendingIntent = NavDeepLinkBuilder(context)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.detailFragment)
        .setArguments(args)
        .createPendingIntent()

    val notification = NotificationCompat.Builder(
        context,
        context.getString(R.string.channel_id)
    ).apply {
        setContentTitle(title)
        setContentText(body)
        setSmallIcon(R.drawable.ic_assistant)
        setContentIntent(pendingIntent)
        setAutoCancel(true)
        addAction(
            0,
            context.getString(R.string.notification_action_label),
            pendingIntent
        )
    }.build()

    notify(NOTIFICATION_ID, notification)

}

fun NotificationManager.cancelAllNotifications()  = cancelAll()

fun createDownloadCompletedNotificationChannel(
    notificationManager: NotificationManager,
    channelId: String,
    channelName: String,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            description = "Download completed"
        }
        notificationManager.createNotificationChannel(channel)
    }
}

fun hasNotificationPermission(applicationContext: Context) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }