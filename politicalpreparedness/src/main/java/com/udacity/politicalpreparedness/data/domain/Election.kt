package com.udacity.politicalpreparedness.data.domain

import java.util.*
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Election(
    val id: Int,
    val name: String,
    val electionDay: Date,
    val division: Division
)

@Parcelize
data class Division(
    val id: String, val country: String, val state: String
) : Parcelable

