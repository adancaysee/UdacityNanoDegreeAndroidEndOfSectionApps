package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocalAsteroidDataSource {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<EntityAsteroid>)

    @Query("SELECT * FROM asteroid_list_table")
    fun getAllAsteroids(): LiveData<List<EntityAsteroid>>

}