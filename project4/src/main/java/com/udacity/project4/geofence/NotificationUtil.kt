package com.udacity.project4.geofence

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.udacity.project4.BuildConfig
import com.udacity.project4.R
import com.udacity.project4.data.domain.Reminder

private const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID + ".channel"
private const val REMINDER_ID = "reminderId"

fun sendReminderNotification(
    context: Context,
    reminder: Reminder,
) {
    val args = Bundle()
    args.putString(REMINDER_ID, reminder.id)

    val notificationManager = getNotificationManager(context)
    val stackBuilder = NavDeepLinkBuilder(context)
        .setGraph(R.navigation.nav_graph)
        .setArguments(args)
        .setDestination(R.id.reminder_detail_destination)
        .createTaskStackBuilder()

    val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_MUTABLE
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }

    val pendingIntent =
        stackBuilder.getPendingIntent(getUniqueId(), flag)

    val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
        setContentTitle(reminder.title)
        setContentText(reminder.locationSnippet)
        setSmallIcon(R.mipmap.ic_launcher)
        setContentIntent(pendingIntent)
        setAutoCancel(true)
    }.build()

    notificationManager.notify(getUniqueId(), notification)

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
