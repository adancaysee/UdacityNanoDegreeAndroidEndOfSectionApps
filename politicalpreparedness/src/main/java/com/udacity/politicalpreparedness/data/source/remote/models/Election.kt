package com.udacity.politicalpreparedness.data.source.remote.models

import com.squareup.moshi.Json
import java.util.*

data class Election(
    val id: Int,
    val name: String,
    val electionDay: Date,
    @Json(name = "ocdDivisionId") val division: Division
)