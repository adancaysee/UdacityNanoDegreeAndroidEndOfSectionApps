package com.udacity.politicalpreparedness.data.source.remote

import com.udacity.politicalpreparedness.data.source.remote.models.NetworkElectionResponse
import com.udacity.politicalpreparedness.data.source.remote.models.NetworkRepresentativeResponse
import com.udacity.politicalpreparedness.data.source.remote.models.NetworkVoterInfoResponse
import retrofit2.http.GET
import retrofit2.http.Query

class CivicsRetrofitNetworkDataSource(
    private val civicsApi: CivicsApi
) : CivicsNetworkDataSource {
    override suspend fun getElections(): NetworkElectionResponse {
        return civicsApi.getElections()
    }

    override suspend fun getVoterInfo(electionId: Int, address: String): NetworkVoterInfoResponse {
        return civicsApi.getVoterInfo(electionId)
    }

    override suspend fun getRepresentatives(address: String): NetworkRepresentativeResponse {
        return civicsApi.getRepresentatives(address)
    }

}

interface CivicsApi {
    @GET("elections")
    suspend fun getElections(): NetworkElectionResponse

    @GET("voterinfo")
    suspend fun getVoterInfo(
        @Query("electionId") electionId: Int,
    ): NetworkVoterInfoResponse

    @GET("representatives")
    suspend fun getRepresentatives(@Query("address") address: String): NetworkRepresentativeResponse
}