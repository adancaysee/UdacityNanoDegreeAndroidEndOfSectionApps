package com.udacity.politicalpreparedness.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ElectionsDao {

    @Query("SELECT * FROM elections_table")
    fun observeSavedElections(): LiveData<List<ElectionEntity>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveElection(electionEntity: ElectionEntity)

    @Query("SELECT * FROM elections_table WHERE id = :id")
    fun getElection(id: Int): ElectionEntity?

    @Query("DELETE FROM elections_table WHERE id = :id" )
    fun deleteElection(id:Int)

    @Query("SELECT * FROM elections_table WHERE id = :id")
    fun observeSavedElection(id: Int): LiveData<ElectionEntity?>
}