package com.udacity.loadapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

class MainViewModel : ViewModel() {

    private val _radioGroupList = MutableLiveData(list)
    val radioGroupList: LiveData<List<DownloadInfo>>
        get() = _radioGroupList

    private var selectedUrl: String? = null


    fun onCheckedChanged(checkedId: Int) {
        val info = list[checkedId - 1]
        selectedUrl = info.url
    }

}