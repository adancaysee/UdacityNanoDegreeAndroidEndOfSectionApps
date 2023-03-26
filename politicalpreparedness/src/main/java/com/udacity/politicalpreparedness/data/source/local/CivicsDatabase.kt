package com.udacity.politicalpreparedness.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [ElectionEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CivicsDatabase : RoomDatabase() {

    abstract val electionsDao: ElectionsDao
}