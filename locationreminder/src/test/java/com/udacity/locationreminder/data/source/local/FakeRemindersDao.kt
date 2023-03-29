package com.udacity.locationreminder.data.source.local

import androidx.lifecycle.LiveData

class FakeRemindersDao(
    var fakeReminders: MutableList<ReminderEntity>? = mutableListOf()
) : RemindersDao {

    override fun observeReminders(): LiveData<List<ReminderEntity>?> {
        throw NotImplementedError("Unused in tests")
    }

    override suspend fun getReminders(): List<ReminderEntity>? {
        return fakeReminders
    }

    override suspend fun getReminderById(reminderId: String): ReminderEntity? {
        return fakeReminders?.find { it.id == reminderId }
    }

    override suspend fun saveReminder(reminder: ReminderEntity) {
        fakeReminders?.add(reminder)
    }

    override suspend fun deleteAllReminders() {
        throw NotImplementedError("Unused in tests")
    }
}