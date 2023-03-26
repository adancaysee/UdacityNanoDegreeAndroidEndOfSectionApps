package com.udacity.politicalpreparedness.data.source.remote.models

import com.squareup.moshi.JsonClass
import com.udacity.politicalpreparedness.data.source.remote.models.Election
import com.udacity.politicalpreparedness.data.source.remote.models.ElectionOfficial
import com.udacity.politicalpreparedness.data.source.remote.models.State

@JsonClass(generateAdapter = true)
class VoterInfoResponse(
    val election: Election,
    val pollingLocations: String? = null, //TODO: Future Use
    val contests: String? = null, //TODO: Future Use
    val state: List<State>? = null,
    val electionElectionOfficials: List<ElectionOfficial>? = null
)