package com.udacity.asteroidradar.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.database.entity.EntityAsteroid
import com.udacity.asteroidradar.util.getCurrentFormattedDate


@Dao
interface LocalAsteroidDataSource {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<EntityAsteroid>)

    @Query("SELECT * FROM asteroid_list_table WHERE closeApproachDate >= :currentDate ORDER BY closeApproachDate ASC")
    fun getAllSortedAsteroidsFromToday(currentDate: String = getCurrentFormattedDate()): LiveData<List<EntityAsteroid>?>

    @Query("SELECT * FROM asteroid_list_table WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC")
    suspend fun getSortedAsteroidsWithFilter(startDate: String?, endDate: String?): List<EntityAsteroid>?

    @Query("SELECT * FROM asteroid_list_table ORDER BY closeApproachDate ASC")
    suspend fun getAllSortedAsteroids(): List<EntityAsteroid>?

    @Query("DELETE FROM asteroid_list_table WHERE closeApproachDate < :endDate ")
    fun deletePreviousAsteroids(endDate: String)

}