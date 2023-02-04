package com.udacity.asteroidradar.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.database.entity.EntityPictureOfDay

@Dao
interface LocalPictureOfDayDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pictureOfDay: EntityPictureOfDay)

    @Query("SELECT * FROM picture_of_day_table ")
    fun getPictureOfDay(): EntityPictureOfDay?

}