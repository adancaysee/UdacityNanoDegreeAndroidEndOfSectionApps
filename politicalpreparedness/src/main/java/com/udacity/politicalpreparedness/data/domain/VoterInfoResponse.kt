package com.udacity.politicalpreparedness.data.domain

class VoterInfo(
    val election: Election,
    val pollingLocations: String? = null,
    val contests: String? = null,
)