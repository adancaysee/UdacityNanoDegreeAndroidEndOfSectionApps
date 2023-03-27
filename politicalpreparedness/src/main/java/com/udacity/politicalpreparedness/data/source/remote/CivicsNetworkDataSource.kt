package com.udacity.politicalpreparedness.data.source.remote

import com.udacity.politicalpreparedness.data.source.remote.models.NetworkElectionResponse
import com.udacity.politicalpreparedness.data.source.remote.models.NetworkRepresentativeResponse
import com.udacity.politicalpreparedness.data.source.remote.models.NetworkVoterInfoResponse

interface CivicsNetworkDataSource {

    suspend fun getElections(): NetworkElectionResponse

    suspend fun getVoterInfo(electionId: Int,address: String): NetworkVoterInfoResponse

    suspend fun getRepresentatives(address: String): NetworkRepresentativeResponse
}

class CivicsRetrofitNetworkDataSource(
    private val civicsApi: CivicsRetrofitApi
) : CivicsNetworkDataSource {
    override suspend fun getElections(): NetworkElectionResponse {
        return civicsApi.getElections()
    }

    override suspend fun getVoterInfo(electionId: Int, address: String): NetworkVoterInfoResponse {
        return civicsApi.getVoterInfo(electionId,address)
    }

    override suspend fun getRepresentatives(address: String): NetworkRepresentativeResponse {
        return civicsApi.getRepresentatives(address)
    }

}



