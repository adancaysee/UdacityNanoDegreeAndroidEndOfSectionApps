package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EntityAsteroid::class,EntityPictureOfDay::class], version = 2, exportSchema = false)
abstract class AsteroidRadarDatabase : RoomDatabase() {

    abstract val localAsteroidDataSource: LocalAsteroidDataSource
    abstract val localPictureOfDayDataSource: LocalPictureOfDayDataSource

    companion object {
        @Volatile
        private var INSTANCE: AsteroidRadarDatabase? = null

        fun getInstance(context: Context): AsteroidRadarDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AsteroidRadarDatabase::class.java,
                    "asteroid_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        INSTANCE = it
                    }

            }
        }
    }
}