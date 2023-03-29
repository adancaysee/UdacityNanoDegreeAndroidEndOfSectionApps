package com.udacity.locationreminder.data.source

import androidx.lifecycle.LiveData
import com.udacity.locationreminder.data.domain.Reminder
import com.udacity.locationreminder.data.source.repository.Result
import com.udacity.locationreminder.data.source.repository.ReminderRepository

class FakeReminderRepository : ReminderRepository {
    override fun observeReminders(): LiveData<List<Reminder>?> {
        throw NotImplementedError("Unused in tests")
    }

    override suspend fun getReminders(): Result<List<Reminder>> {
        throw NotImplementedError("Unused in tests")
    }

    override suspend fun saveReminder(reminder: Reminder) {
        throw NotImplementedError("Unused in tests")
    }

    override suspend fun getReminder(id: String): Result<Reminder> {
        throw NotImplementedError("Unused in tests")
    }

    override suspend fun deleteAllReminders() {
        throw NotImplementedError("Unused in tests")
    }
}