package com.udacity.politicalpreparedness.data.source.remote

import com.udacity.politicalpreparedness.data.source.remote.models.NetworkElectionResponse
import com.udacity.politicalpreparedness.data.source.remote.models.NetworkRepresentativeResponse
import com.udacity.politicalpreparedness.data.source.remote.models.NetworkVoterInfoResponse

interface CivicsNetworkDataSource {

    suspend fun getElections(): NetworkElectionResponse

    suspend fun getVoterInfo(address: String, electionId: Int): NetworkVoterInfoResponse

    suspend fun getRepresentatives(address: String): NetworkRepresentativeResponse
}



