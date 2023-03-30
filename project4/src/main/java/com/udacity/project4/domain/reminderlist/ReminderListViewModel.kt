package com.udacity.project4.domain.reminderlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.udacity.project4.base.BaseViewModel
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.data.domain.Reminder
import com.udacity.project4.data.source.repository.ReminderRepository
import kotlinx.coroutines.launch

class ReminderListViewModel(
    private val application: Application,
    private val reminderRepository: ReminderRepository
) :
    BaseViewModel(application) {

    private val _reminders = reminderRepository.observeReminders()
    val reminders: LiveData<List<Reminder>?> = _reminders

    val showNoData: LiveData<Boolean> = Transformations.map(reminders) { it.isNullOrEmpty() }

    fun navigateToAddReminder(command: NavigationCommand) {
        navigationCommandEvent.value = command
    }

    fun navigateToReminderDetail(command: NavigationCommand) {
        navigationCommandEvent.value = command
    }

    fun logout(navigationCommand: NavigationCommand) {
        AuthUI.getInstance()
            .signOut(application)
            .addOnCompleteListener {
                viewModelScope.launch {
                    reminderRepository.deleteAllReminders()
                    navigationCommandEvent.value = navigationCommand
                }
            }

    }
}