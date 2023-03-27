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

data class NetworkState(
    val name: String,
    val electionAdministrationBody: NetworkAdministrationBody
)

@JsonClass(generateAdapter = true)
data class NetworkAdministrationBody(
    val name: String? = null,
    val electionInfoUrl: String? = null,
    val votingLocationFinderUrl: String? = null,
    val ballotInfoUrl: String? = null,
    val correspondenceAddress: NetworkAddress? = null
)

fun NetworkVoterInfoResponse.asDomain() = VoterInfo(
    election = this.election.asDomain(),
    pollingLocations = this.pollingLocations,
    contests = this.contests

)