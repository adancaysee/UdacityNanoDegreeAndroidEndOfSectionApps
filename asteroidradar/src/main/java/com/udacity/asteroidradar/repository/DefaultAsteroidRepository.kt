package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.LocalAsteroidDataSource
import com.udacity.asteroidradar.database.asDomain
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.asDatabase
import com.udacity.asteroidradar.network.*
import com.udacity.asteroidradar.util.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

sealed class NEoWsApiStatus {
    object Loading : NEoWsApiStatus()
    object Success : NEoWsApiStatus()
    data class Failure(val errorMsg: String) : NEoWsApiStatus()
}


class DefaultAsteroidRepository(
    private val remoteAsteroidDataSource: RemoteAsteroidDataSource,
    private val localAsteroidDataSource: LocalAsteroidDataSource
) : AsteroidRepository {

    override val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(localAsteroidDataSource.getAllSortedAsteroids()) {
            it.asDomain()
        }

    override suspend fun fetchAsteroids() {
        withContext(Dispatchers.IO) {
            val jsonString = remoteAsteroidDataSource.getAsteroids()
            val list = parseAsteroidsJsonResult(JSONObject(jsonString))
            localAsteroidDataSource.insertAll(list.asDatabase())
        }
    }



}