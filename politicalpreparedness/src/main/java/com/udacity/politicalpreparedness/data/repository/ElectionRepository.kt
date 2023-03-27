package com.udacity.politicalpreparedness.data.repository

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

interface ElectionRepository {

    suspend fun fetchUpcomingElections(): Result<List<Election>>

    fun observeSavedElections(): LiveData<List<Election>?>

    suspend fun saveElection(electionEntity: ElectionEntity)

    fun observeSavedElection(electionId: Int): LiveData<Election?>

    suspend fun getSavedElection(electionId: Int): Result<Election>

    suspend fun deleteElection(electionId: Int)

}

class DefaultElectionRepository(
    private val electionsDao: ElectionsDao,
    private val civicsNetworkDataSource: CivicsNetworkDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectionRepository {

    override suspend fun fetchUpcomingElections(): Result<List<Election>> =
        withContext(dispatcher) {
            return@withContext try {
                val data = civicsNetworkDataSource.getElections().elections
                Result.Success(data.asDomain())
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }

    override fun observeSavedElections(): LiveData<List<Election>?> =
        Transformations.map(electionsDao.observeSavedElections()) {
            it?.asDomain()
        }

    override suspend fun saveElection(electionEntity: ElectionEntity) = withContext(dispatcher) {
        electionsDao.saveElection(electionEntity)
    }

    override fun observeSavedElection(electionId: Int): LiveData<Election?> =
        Transformations.map(electionsDao.observeSavedElection(electionId)) {
            it?.asDomain()
        }

    override suspend fun getSavedElection(electionId: Int): Result<Election> =
        withContext(dispatcher) {
            return@withContext try {
                val data = electionsDao.getElection(electionId)
                if (data != null) {
                    Result.Success(data.asDomain())
                } else {
                    Result.Error(Exception("Not found"))
                }
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }


    override suspend fun deleteElection(electionId: Int) = withContext(dispatcher) {
        electionsDao.deleteElection(electionId)
    }
}

