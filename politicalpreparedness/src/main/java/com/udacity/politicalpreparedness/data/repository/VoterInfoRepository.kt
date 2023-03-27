package com.udacity.politicalpreparedness.data.repository

import com.udacity.politicalpreparedness.data.domain.VoterInfo
import com.udacity.politicalpreparedness.data.source.remote.CivicsNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.udacity.politicalpreparedness.data.source.remote.models.asDomain

interface VoterInfoRepository {

    suspend fun fetchVoterInfo(electionId: Int, address: String): Result<VoterInfo>
}

class DefaultVoterInfoRepository(
    private val civicsNetworkDataSource: CivicsNetworkDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : VoterInfoRepository {

    override suspend fun fetchVoterInfo(electionId: Int, address: String): Result<VoterInfo> =
        withContext(dispatcher) {
            return@withContext try {
                val data = civicsNetworkDataSource.getVoterInfo(electionId, address)
                Result.Success(data.asDomain())
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }

}