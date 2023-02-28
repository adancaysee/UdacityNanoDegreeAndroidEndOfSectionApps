package com.udacity.loadapp.main

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.widget.Toast
import com.udacity.loadapp.util.getDownloadManager

class DownloadCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        val query = DownloadManager.Query()
        query.setFilterById(id)

        val c: Cursor = getDownloadManager(context).query(query)
        if (c.moveToFirst()) {
            val columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS)
            if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                Toast.makeText(context, "success", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "failure", Toast.LENGTH_LONG).show()
            }
        }
    }

}