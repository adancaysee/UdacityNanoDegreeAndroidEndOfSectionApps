package com.udacity.politicalpreparedness.data.source.remote.models

import com.udacity.politicalpreparedness.data.domain.Representative


data class NetworkRepresentative(
    val official: NetworkOfficial,
    val office: NetworkOffice
)

fun List<NetworkRepresentative>.asDomain() = map {
    Representative(
        it.official.asDomain(),
        it.office.asDomain()
    )
}