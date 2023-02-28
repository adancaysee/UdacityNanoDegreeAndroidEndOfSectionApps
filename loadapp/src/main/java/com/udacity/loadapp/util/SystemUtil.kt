package com.udacity.loadapp.util

import android.app.DownloadManager
import android.content.Context

fun getDownloadManager(context: Context) =
    context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager