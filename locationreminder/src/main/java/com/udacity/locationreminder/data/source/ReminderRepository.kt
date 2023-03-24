package com.udacity.locationreminder.data.source

import androidx.lifecycle.LiveData
import com.udacity.locationreminder.data.domain.Reminder
import com.udacity.locationreminder.data.domain.Result

interface ReminderRepository {
    fun observeReminders(): LiveData<List<Reminder>?>

    suspend fun getReminders(): Result<List<Reminder>>

    suspend fun saveReminder(reminder: Reminder)

    suspend fun getReminder(id: String): Result<Reminder>

    suspend fun deleteAllReminders()
}