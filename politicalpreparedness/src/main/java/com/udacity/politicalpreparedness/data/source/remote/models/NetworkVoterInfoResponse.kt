package com.udacity.politicalpreparedness.data.source.remote.models

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json
import com.udacity.politicalpreparedness.data.domain.Contest
import com.udacity.politicalpreparedness.data.domain.PollingLocation
import com.udacity.politicalpreparedness.data.domain.VoterInfo

@JsonClass(generateAdapter = true)
class NetworkVoterInfoResponse(
    val election: NetworkElection,
    val pollingLocations: List<NetworkPollingLocation>? = null, //TODO: Future Use
    val contests: List<NetworkContest>? = null, //TODO: Future Use
    val state: List<NetworkState>? = null,
    val electionElectionOfficials: List<NetworkElectionOfficial>? = null
)
data class NetworkPollingLocation(val address: NetworkAddress, val pollingHours: String)

data class NetworkContest(@Json(name = "type") val type: String, val office: String?)

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

fun List<NetworkPollingLocation>.asDomain(): List<PollingLocation> = map {
    PollingLocation(it.address, it.pollingHours)
}

/*fun List<Contest>.asDomain(): List<Contest> = map {
    Contest(it.type, it.office)
}*/

fun NetworkVoterInfoResponse.asDomain() = VoterInfo(
    election = this.election.asDomain(),
    pollingLocations = this.pollingLocations?.asDomain(),
    //contests = this.contests?.asDomain()
)