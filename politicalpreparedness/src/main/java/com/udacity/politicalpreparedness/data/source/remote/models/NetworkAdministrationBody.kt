package com.udacity.politicalpreparedness.data.source.remote.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkAdministrationBody(
    val name: String? = null,
    val electionInfoUrl: String? = null,
    val votingLocationFinderUrl: String? = null,
    val ballotInfoUrl: String? = null,
    val correspondenceAddress: NetworkAddress? = null
)