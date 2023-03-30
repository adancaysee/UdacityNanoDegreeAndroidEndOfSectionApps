package com.udacity.locationreminder.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.locationreminder.data.domain.Reminder
import com.udacity.locationreminder.data.source.repository.ReminderRepository
import kotlinx.coroutines.runBlocking
import com.udacity.locationreminder.data.source.repository.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import java.lang.Exception

@ExperimentalCoroutinesApi
class FakeReminderRepository : ReminderRepository {

    var fakeReminders = mutableListOf<Reminder>()

    private val observableTasks = MutableLiveData<List<Reminder>?>()

    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override fun observeReminders(): LiveData<List<Reminder>?> {
        runBlocking { refreshReminders() }
        return observableTasks
    }

    override suspend fun getReminders(): Result<List<Reminder>> {
        if (shouldReturnError) {
            return Result.Error(Exception("test"))
        }
        return Result.Success(fakeReminders)
    }

    override suspend fun saveReminder(reminder: Reminder) {
        if (shouldReturnError) {
            return
        }
        fakeReminders.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<Reminder> {
        if (shouldReturnError) {
            return Result.Error(Exception("test"))
        }
        val reminder =
            fakeReminders.find { it.id == id } ?: return Result.Error(Exception("not found"))
        return Result.Success(reminder)
    }

    override suspend fun deleteAllReminders() {
        fakeReminders.clear()
    }

    private fun refreshReminders() = runTest{
        val result = getReminders()
        if (result is Result.Success) {
            observableTasks.value = result.data
        }else {
            observableTasks.value = null
        }

    }
}