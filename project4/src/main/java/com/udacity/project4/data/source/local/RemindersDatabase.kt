package com.udacity.project4.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ReminderEntity::class], version = 1, exportSchema = false)
abstract class RemindersDatabase : RoomDatabase() {

    abstract val remindersDao: RemindersDao
}