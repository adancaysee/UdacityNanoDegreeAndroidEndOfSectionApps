package com.udacity.project4.domain.reminderdetail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.project4.base.BaseViewModel
import com.udacity.project4.data.domain.Reminder
import com.udacity.project4.data.source.repository.ReminderRepository
import kotlinx.coroutines.launch
import com.udacity.project4.data.source.repository.Result

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