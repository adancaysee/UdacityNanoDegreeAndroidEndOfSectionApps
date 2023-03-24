package com.udacity.locationreminder.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemindersDao {
    @Query("SELECT * FROM reminders_table")
    fun observeReminders(): LiveData<List<ReminderEntity>?>

    @Query("SELECT * FROM reminders_table")
    suspend fun getReminders(): List<ReminderEntity>?

    @Query("SELECT * FROM reminders_table WHERE id = :reminderId")
    suspend fun getReminderById(reminderId: String): ReminderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReminder(reminder: ReminderEntity)

    @Query("DELETE FROM reminders")
    suspend fun deleteAllReminders()
}