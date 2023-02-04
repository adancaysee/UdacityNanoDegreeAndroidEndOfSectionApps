package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.dao.LocalAsteroidDataSource
import com.udacity.asteroidradar.database.entity.asDomain
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.asDatabase
import com.udacity.asteroidradar.network.*
import com.udacity.asteroidradar.util.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

interface AsteroidRepository {
    val asteroids: LiveData<List<Asteroid>>
    suspend fun refreshAsteroids(startDate: String?, endDate: String?)
    suspend fun deletePreviousAsteroids(endDate: String)
}

class DefaultAsteroidRepository(
    private val remoteAsteroidDataSource: RemoteAsteroidDataSource,
    private val localAsteroidDataSource: LocalAsteroidDataSource
) : AsteroidRepository {

    override val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(localAsteroidDataSource.getAllSortedAsteroids()) {
            it.asDomain()
        }

    override suspend fun refreshAsteroids(startDate: String?, endDate: String?) {
        withContext(Dispatchers.IO) {
            val jsonString =
                remoteAsteroidDataSource.getAsteroids(startDate = startDate, endDate = endDate)
            val list = parseAsteroidsJsonResult(JSONObject(jsonString))
            localAsteroidDataSource.insertAll(list.asDatabase())
        }
    }

    override suspend fun deletePreviousAsteroids(endDate: String) {
        withContext(Dispatchers.IO) {
            localAsteroidDataSource.deletePreviousAsteroids(endDate)
        }
    }
}

sealed class NEoWsApiStatus {
    object Loading : NEoWsApiStatus()
    object Success : NEoWsApiStatus()
    data class Failure(val errorMsg: String) : NEoWsApiStatus()
}

