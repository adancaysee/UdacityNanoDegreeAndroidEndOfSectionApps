package com.udacity

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.loadapp.util.getDownloadManager
import java.io.File

sealed class DownloadStatus {
    object None : DownloadStatus()
    object Downloading : DownloadStatus()
    object Success : DownloadStatus()
    object Failure : DownloadStatus()
}

class DownloadFileRepository {

    private var downloadID = -1L

    private val _downloadStatus = MutableLiveData<DownloadStatus>()
    val downloadStatus: LiveData<DownloadStatus>
        get() = _downloadStatus

    fun downloadFileFromUrl(
        context: Context,
        url: String,
        title: String,
        description: String,
    ) {
        reset()
        val fileName =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/Contents"
        val file = File(fileName)
        val request = DownloadManager.Request(Uri.parse(url))
            .setDestinationUri(Uri.fromFile(file))
            .setTitle(title)
            .setDescription(description)
        try {
            _downloadStatus.value = DownloadStatus.Downloading
            downloadID = getDownloadManager(context).enqueue(request)
        } catch (_: Exception) {
            _downloadStatus.value = DownloadStatus.Failure
        }
    }

    fun reset() {
        _downloadStatus.value = DownloadStatus.None
    }

    fun update(status: DownloadStatus) {
        _downloadStatus.value = status
    }
}