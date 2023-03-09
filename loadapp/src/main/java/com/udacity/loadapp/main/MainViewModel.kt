package com.udacity.loadapp.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.udacity.loadapp.LoadAppApplication
import com.udacity.loadapp.repository.DownloadFileRepository
import com.udacity.loadapp.repository.DownloadStatus
import com.udacity.loadapp.util.ButtonState
import com.udacity.loadapp.R

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

class MainViewModel(
    private val application: Application,
    private val repository: DownloadFileRepository
) : AndroidViewModel(application) {

    private val _radioGroupList = MutableLiveData(list)
    val radioGroupList: LiveData<List<DownloadInfo>>
        get() = _radioGroupList

    private var selectedDownloadInfo: DownloadInfo? = null

    private val _emptySelectionEvent = MutableLiveData<Boolean>()
    val emptySelectionEvent: LiveData<Boolean>
        get() = _emptySelectionEvent

    private val _buttonState = Transformations.map(repository.downloadStatus) {
        when (it) {
            is DownloadStatus.Downloading -> ButtonState.Loading
            is DownloadStatus.Success, is DownloadStatus.Failure -> {
                ButtonState.Completed
            }
            else -> ButtonState.None
        }
    }
    val buttonState: LiveData<ButtonState>
        get() = _buttonState

    init {
        repository.reset()
    }

    fun onCheckedChanged(checkedId: Int) {
        val info = list[checkedId]
        selectedDownloadInfo = info
    }

    fun downloadFromUrl() {
        if (selectedDownloadInfo == null) {
            _emptySelectionEvent.value = true
            return
        }
        repository.downloadFileFromUrl(
            application,
            selectedDownloadInfo!!.url,
            selectedDownloadInfo!!.title,
            application.getString(R.string.app_description)
        )
    }

    fun doneEmptySelectionEvent() {
        _emptySelectionEvent.value = false
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as LoadAppApplication
                MainViewModel(application, application.downloadFileRepository)
            }
        }
    }

}