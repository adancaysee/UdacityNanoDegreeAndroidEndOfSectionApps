package com.udacity.politicalpreparedness.data.domain

import com.udacity.politicalpreparedness.data.source.remote.models.NetworkAddress

class VoterInfo(
    val election: Election,
    val pollingLocations: List<PollingLocation>? = null,
    val contests: List<Contest>? = null,
)

data class PollingLocation (
    val address: NetworkAddress,
    val pollingHours:String,
)

data class Contest (
    val type:String,
    val office: String?
)