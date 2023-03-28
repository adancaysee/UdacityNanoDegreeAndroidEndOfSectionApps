package com.udacity.politicalpreparedness.data.source.remote.models

import com.squareup.moshi.JsonClass
import com.udacity.politicalpreparedness.data.domain.VoterInfo


@JsonClass(generateAdapter = true)
class NetworkVoterInfoResponse(
    val election: NetworkElection,
    val kind: String?,
    val normalizedInput: NetworkAddress?,
    val state: List<NetworkState>?
)

fun NetworkVoterInfoResponse.asDomain():VoterInfo = VoterInfo(
    election = election.asDomain(),
    ballotInfoUrl = state?.get(0)?.electionAdministrationBody?.ballotInfoUrl,
    votingLocationFinderUrl = state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl

)



data class NetworkState(
    val electionAdministrationBody: NetworkElectionAdministrationBody?,
    val name: String?,
    val sources: List<NetworkSource>?
)


data class NetworkElectionAdministrationBody(
    val absenteeVotingInfoUrl: String?,
    val ballotInfoUrl: String?,
    val electionInfoUrl: String?,
    val electionRegistrationConfirmationUrl: String?,
    val electionRegistrationUrl: String?,
    val name: String?,
    val votingLocationFinderUrl: String?
)

data class NetworkSource(
    val name: String,
    val official: Boolean
)
