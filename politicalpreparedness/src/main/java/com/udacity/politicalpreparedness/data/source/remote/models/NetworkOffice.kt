package com.udacity.politicalpreparedness.data.source.remote.models

import com.squareup.moshi.Json
import com.udacity.politicalpreparedness.data.domain.Office

data class NetworkOffice(
    val name: String,
    @Json(name = "divisionId") val division: NetworkDivision,
    @Json(name = "officialIndices") val officials: List<Int>
) {
    fun getRepresentatives(officials: List<NetworkOfficial>): List<NetworkRepresentative> {
        return this.officials.map { index ->
            NetworkRepresentative(officials[index], this)
        }
    }
}

fun NetworkOffice.asDomain() = Office(
    name = this.name,
    division = this.division.asDomain(),
    officials = this.officials
)
