package com.udacity.politicalpreparedness.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface ElectionsDao {

    @Query("SELECT * FROM elections_table")
    fun observeSavedElections(): LiveData<List<ElectionEntity>?>
}