package com.udacity.politicalpreparedness.data.domain

import java.util.*

data class Election(
    val id: Int,
    val name: String,
    val electionDay: Date,
    val division: Division
)

data class Division(
    val id: String, val country: String, val state: String
)

