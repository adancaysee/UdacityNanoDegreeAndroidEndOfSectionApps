package com.udacity.locationreminder.domain.savereminder

import android.app.Application
import com.udacity.locationreminder.base.BaseViewModel
import com.udacity.locationreminder.data.source.repository.ReminderRepository

class SaveReminderViewModel(application: Application, reminderRepository: ReminderRepository) :
    BaseViewModel(application) {
}