package com.udacity.politicalpreparedness.data.source.remote.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkElectionResponse(
    val kind: String,
    val elections: List<NetworkElection>
)