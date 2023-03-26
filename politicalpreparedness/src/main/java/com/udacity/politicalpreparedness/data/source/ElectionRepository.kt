package com.udacity.politicalpreparedness.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.politicalpreparedness.data.domain.Election
import com.udacity.politicalpreparedness.data.source.local.ElectionsDao
import com.udacity.politicalpreparedness.data.source.remote.CivicsNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import com.udacity.politicalpreparedness.data.domain.Result
import com.udacity.politicalpreparedness.data.source.local.ElectionEntity
import com.udacity.politicalpreparedness.data.source.local.asDomain
import com.udacity.politicalpreparedness.data.source.remote.models.asDomain
import kotlinx.coroutines.Dispatchers

interface CivicsRepository {

    suspend fun fetchUpcomingSelections(): Result<List<Election>>

    fun observeSavedElections(): LiveData<List<Election>?>

    suspend fun saveElection(electionEntity: ElectionEntity)


}

class DefaultCivicsRepository(
    private val electionsDao: ElectionsDao,
    private val civicsNetworkDataSource: CivicsNetworkDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CivicsRepository {

    override suspend fun fetchUpcomingSelections(): Result<List<Election>> {
        return withContext(dispatcher) {
            return@withContext try {
                val data = civicsNetworkDataSource.getElections().elections
                Result.Success(data.asDomain())
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }
    }

    override fun observeSavedElections(): LiveData<List<Election>?> =
        Transformations.map(electionsDao.observeSavedElections()) {
            it?.asDomain()
        }

    override suspend fun saveElection(electionEntity: ElectionEntity) {
        withContext(dispatcher) {
            electionsDao
        }
    }

}

