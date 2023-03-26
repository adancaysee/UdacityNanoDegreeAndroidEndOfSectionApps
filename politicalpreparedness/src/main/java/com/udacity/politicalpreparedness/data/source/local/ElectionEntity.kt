package com.udacity.politicalpreparedness.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "elections_table")
data class ElectionEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "electionDay") val electionDay: Date,
    @Embedded(prefix = "division_") val division: LocalDivision
)

data class LocalDivision(
    val id: String,
    val country: String,
    val state: String
)