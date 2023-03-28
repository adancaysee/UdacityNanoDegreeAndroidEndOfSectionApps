package com.udacity.locationreminder.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.udacity.locationreminder.BuildConfig
import com.udacity.locationreminder.R

private const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID + ".channel"

fun NotificationManager.sendNotification(
    context: Context,
    title: String,
    body: String,
    args: Bundle,
) {
    val stackBuilder = NavDeepLinkBuilder(context)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.reminder_detail_destination)
        .setArguments(args)
        .createTaskStackBuilder()

    val pendingIntent =
        stackBuilder.getPendingIntent(getUniqueId(), PendingIntent.FLAG_UPDATE_CURRENT)

    val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
        setContentTitle(title)
        setContentText(body)
        setSmallIcon(R.mipmap.ic_launcher)
        setContentIntent(pendingIntent)
        setAutoCancel(true)
    }.build()

    notify(getUniqueId(), notification)

}

private fun getUniqueId() = ((System.currentTimeMillis() % 10000).toInt())

fun NotificationManager.cancelAllNotifications() = cancelAll()

fun createNotificationChannel(
    context: Context,
) {
    val notificationManager = getNotificationManager(context)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        && notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null
    ) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            context.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            description = "Entered geo"
        }
        notificationManager.createNotificationChannel(channel)
    }
}

fun getNotificationManager(context: Context): NotificationManager =
    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

fun hasNotificationPermission(applicationContext: Context) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }