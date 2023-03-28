package com.udacity.locationreminder.domain.reminderlist

import android.app.Application
import com.udacity.locationreminder.base.BaseViewModel
import com.udacity.locationreminder.data.source.ReminderRepository

class ReminderListViewModel(application: Application, reminderRepository: ReminderRepository) :
    BaseViewModel(application) {
}