package com.udacity.locationreminder.domain.reminderdetail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.locationreminder.base.BaseViewModel
import com.udacity.locationreminder.data.domain.Reminder
import com.udacity.locationreminder.data.source.repository.ReminderRepository
import kotlinx.coroutines.launch
import com.udacity.locationreminder.data.source.repository.Result

class ReminderDetailViewModel(
    application: Application,
    private val reminderRepository: ReminderRepository
) : BaseViewModel(application) {

    private val reminderId = MutableLiveData<String>()

    private val _reminder = MutableLiveData<Reminder>()
    val reminder: LiveData<Reminder> = _reminder

    fun setParameters(reminderId: String) {
        //for configuration changes
        if (this.reminderId.value == reminderId && showLoading.value == true) return
        this.reminderId.value = reminderId
    }

    fun findReminder() {
        viewModelScope.launch {
            val result = reminderId.value?.let { reminderRepository.getReminder(it) }
            if (result is Result.Success) {
                val data = result.data
                _reminder.value = data
            } else {
                showErrorMessageEvent.value = "Not found"
            }
        }
    }

}