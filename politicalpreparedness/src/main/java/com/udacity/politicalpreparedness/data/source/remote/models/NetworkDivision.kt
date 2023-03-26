package com.udacity.politicalpreparedness.data.source.remote.models

import com.udacity.politicalpreparedness.data.domain.Division

data class NetworkDivision(
    val id: String, val country: String, val state: String
)

fun NetworkDivision.asDomain() = Division(id, country, state)