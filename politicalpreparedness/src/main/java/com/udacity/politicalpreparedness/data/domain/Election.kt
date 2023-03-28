package com.udacity.politicalpreparedness.data.domain

import java.util.*
import android.os.Parcelable
import com.udacity.politicalpreparedness.data.source.local.ElectionEntity
import com.udacity.politicalpreparedness.data.source.local.LocalDivision
import kotlinx.parcelize.Parcelize

data class Election(
    val id: Int,
    val name: String,
    val electionDay: Date,
    val division: Division
)

fun Election.asDatabase(): ElectionEntity =
    ElectionEntity(this.id, this.name, this.electionDay, this.division.asDatabase())

@Parcelize
data class Division(
    val id: String, val country: String, val state: String
) : Parcelable

fun Division.asDatabase(): LocalDivision = LocalDivision(
    this.id, this.country, this.state
)

