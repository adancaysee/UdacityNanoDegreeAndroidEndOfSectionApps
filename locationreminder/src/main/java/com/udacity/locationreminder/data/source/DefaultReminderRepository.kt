package com.udacity.locationreminder.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.locationreminder.data.domain.Reminder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.udacity.locationreminder.data.domain.Result
import com.udacity.locationreminder.data.domain.asDatabase
import com.udacity.locationreminder.data.source.local.RemindersDao
import com.udacity.locationreminder.data.source.local.asDomain

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
            Result.Success(reminders!!.asDomain())
        } catch (ex: Exception) {
            Result.Error(Exception(ex.localizedMessage ?: "unknown exception"))
        }
    }

    override suspend fun saveReminder(reminder: Reminder) = withContext(coroutineDispatcher) {
        remindersDao.saveReminder(reminder.asDatabase())
    }

    override suspend fun getReminder(id: String): Result<Reminder> = withContext(coroutineDispatcher) {
        return@withContext try {
            val reminder = remindersDao.getReminderById(id)
            Result.Success(reminder!!.asDomain())
        }catch (ex:Exception) {
            Result.Error(Exception("Not found reminder"))
        }
    }

    override suspend fun deleteAllReminders() = withContext(coroutineDispatcher) {
        remindersDao.deleteAllReminders()
    }
}