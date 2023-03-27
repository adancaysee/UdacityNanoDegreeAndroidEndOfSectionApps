package com.udacity.politicalpreparedness.data.repository

import com.udacity.politicalpreparedness.data.domain.Representative
import com.udacity.politicalpreparedness.data.source.remote.CivicsNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.udacity.politicalpreparedness.data.source.remote.models.NetworkRepresentative
import com.udacity.politicalpreparedness.data.source.remote.models.asDomain


interface RepresentativeRepository {
    suspend fun fetchRepresentatives(): Result<List<Representative>>
}

class DefaultRepresentativeRepository(
    private val civicsNetworkDataSource: CivicsNetworkDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RepresentativeRepository {

    override suspend fun fetchRepresentatives(): Result<List<Representative>> =
        withContext(dispatcher) {
            return@withContext try {
                val response =
                    civicsNetworkDataSource.getRepresentatives("1263 Pacific Ave. Kansas City KS")
                val representatives:List<NetworkRepresentative> = response.offices.flatMap {
                    it.getRepresentatives(response.officials)
                }
                Result.Success(representatives.asDomain())
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }


}