package com.udacity.politicalpreparedness.data.domain



data class VoterInfo(
    val election: Election,
    val ballotInfoUrl: String?,
    val votingLocationFinderUrl: String?

)


