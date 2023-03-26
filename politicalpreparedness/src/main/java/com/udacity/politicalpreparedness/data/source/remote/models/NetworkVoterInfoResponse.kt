package com.udacity.politicalpreparedness.data.source.remote.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class NetworkVoterInfoResponse(
    val election: NetworkElection,
    val pollingLocations: String? = null, //TODO: Future Use
    val contests: String? = null, //TODO: Future Use
    val state: List<NetworkState>? = null,
    val electionElectionOfficials: List<NetworkElectionOfficial>? = null
)