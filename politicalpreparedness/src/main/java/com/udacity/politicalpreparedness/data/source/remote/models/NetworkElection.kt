package com.udacity.politicalpreparedness.data.source.remote.models

import com.squareup.moshi.Json
import com.udacity.politicalpreparedness.data.domain.Election
import java.util.*
import kotlin.collections.List

data class NetworkElection(
    val id: Int,
    val name: String,
    val electionDay: Date,
    @Json(name = "ocdDivisionId") val division: NetworkDivision
)

fun List<NetworkElection>.asDomain(): List<Election> = map {
    Election(
        it.id, it.name, it.electionDay, it.division.asDomain()
    )
}

fun NetworkElection.asDomain(): Election = Election(
    this.id, this.name, this.electionDay, this.division.asDomain()
)