package com.udacity.loadapp.main

import android.app.Application
import android.app.DownloadManager
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.loadapp.ButtonState
import com.udacity.loadapp.R
import com.udacity.loadapp.util.getDownloadManager

data class DownloadInfo(
    val title: String,
    val url: String
)

private val list = listOf(
    DownloadInfo(
        title = "Glide - Image Loading Library by BumpTech",
        url = "https://github.com/bumptech/glide"
    ),
    DownloadInfo(
        title = "LoadApp - Current repository by Udacity",
        url = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
    ),
    DownloadInfo(
        title = "Retrofit - Type-safe HTTP client for Android and Java by Square,Inc",
        url = "https://github.com/square/retrofit"
    )
)

class MainViewModel(private val application: Application) : AndroidViewModel(application) {

    private val _radioGroupList = MutableLiveData(list)
    val radioGroupList: LiveData<List<DownloadInfo>>
        get() = _radioGroupList

    private var selectedDownloadInfo: DownloadInfo? = null

    private val _buttonState = MutableLiveData<ButtonState>()
    val buttonState: LiveData<ButtonState>
        get() = _buttonState

    private val _emptySelectionEvent = MutableLiveData<Boolean>()
    val emptySelectionEvent: LiveData<Boolean>
        get() = _emptySelectionEvent

    private var downloadID: Long = 0

    fun onCheckedChanged(checkedId: Int) {
        val info = list[checkedId]
        selectedDownloadInfo = info
    }

    fun downloadFromUrl() {
        if (selectedDownloadInfo == null) {
            _emptySelectionEvent.value = true
            return
        }
        _buttonState.value = ButtonState.Clicked

        val request = DownloadManager.Request(Uri.parse(selectedDownloadInfo!!.url))
            .setTitle(selectedDownloadInfo!!.title)
            .setDescription(application.getString(R.string.app_description))
        try {
            downloadID = getDownloadManager(application.applicationContext).enqueue(request)
        } catch (_: Exception) {
        }
    }

    fun doneEmptySelectionEvent() {
        _emptySelectionEvent.value = false
    }

}