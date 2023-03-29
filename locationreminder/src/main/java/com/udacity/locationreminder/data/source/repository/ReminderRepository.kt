package com.udacity.locationreminder.data.source.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.locationreminder.data.domain.Reminder
import com.udacity.locationreminder.data.domain.asDatabase
import com.udacity.locationreminder.data.source.local.RemindersDao
import com.udacity.locationreminder.data.source.local.asDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ReminderRepository {
    fun observeReminders(): LiveData<List<Reminder>?>

    suspend fun getReminders(): Result<List<Reminder>>

    suspend fun saveReminder(reminder: Reminder)

    suspend fun getReminder(id: String): Result<Reminder>

    suspend fun deleteAllReminders()
}

class DefaultReminderRepository(
    private val remindersDao: RemindersDao,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ReminderRepository {

    override fun observeReminders(): LiveData<List<Reminder>?> {
        return Transformations.map(remindersDao.observeReminders()) {
            it?.asDomain()
        }
    }

    override suspend fun getReminders(): Result<List<Reminder>> = withContext(coroutineDispatcher) {
        return@withContext try {
            val reminders = remindersDao.getReminders()
            if (reminders != null) {
                Result.Success(reminders.asDomain())
            } else {
                Result.Error(Exception("Not found reminders"))
            }
        } catch (ex: Exception) {
            Result.Error(Exception(ex.localizedMessage ?: "unknown exception"))
        }
    }

    override suspend fun saveReminder(reminder: Reminder) = withContext(coroutineDispatcher) {
        remindersDao.saveReminder(reminder.asDatabase())
    }

    override suspend fun getReminder(id: String): Result<Reminder> =
        withContext(coroutineDispatcher) {
            return@withContext try {
                val reminder = remindersDao.getReminderById(id)
                if (reminder != null) {
                    Result.Success(reminder.asDomain())
                } else {
                    Result.Error(Exception("Not found reminder"))
                }
            } catch (ex: Exception) {
                Result.Error(Exception(ex.localizedMessage ?: "unknown exception"))
            }
        }

    override suspend fun deleteAllReminders() = withContext(coroutineDispatcher) {
        remindersDao.deleteAllReminders()
    }
}