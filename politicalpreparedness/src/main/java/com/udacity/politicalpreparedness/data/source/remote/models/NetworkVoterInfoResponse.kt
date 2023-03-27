package com.udacity.politicalpreparedness.data.source.remote.models

import com.squareup.moshi.JsonClass
import com.udacity.politicalpreparedness.data.domain.VoterInfo

@JsonClass(generateAdapter = true)
class NetworkVoterInfoResponse(
    val election: NetworkElection,
    val pollingLocations: String? = null, //TODO: Future Use
    val contests: String? = null, //TODO: Future Use
    val state: List<NetworkState>? = null,
    val electionElectionOfficials: List<NetworkElectionOfficial>? = null
)

fun NetworkVoterInfoResponse.asDomain() = VoterInfo(
    election = this.election.asDomain(),
    pollingLocations = this.pollingLocations,
    contests = this.contests

)