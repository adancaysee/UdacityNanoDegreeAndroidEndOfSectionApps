package com.udacity.loadapp.util

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import com.udacity.loadapp.R

class DownloadCompleteReceiver : BroadcastReceiver() {

    @SuppressLint("Range")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

        val query = DownloadManager.Query()
        query.setFilterById(id)

        val fileName: String
        val c: Cursor = getDownloadManager(context).query(query)
        if (c.moveToFirst()) {
            val status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
            fileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE))
            val statusStr = if (DownloadManager.STATUS_SUCCESSFUL == status) {
                context.getString(R.string.success)
            } else {
                context.getString(R.string.fail)
            }

            val args = Bundle()
            args.putString("status", statusStr)
            args.putString("fileName", fileName)

            getNotificationManager(context).cancelAllNotifications()

            getNotificationManager(context).sendNotification(
                context,
                context.getString(R.string.notification_title),
                context.getString(R.string.notification_description,fileName),
                args
            )
        }
    }

}