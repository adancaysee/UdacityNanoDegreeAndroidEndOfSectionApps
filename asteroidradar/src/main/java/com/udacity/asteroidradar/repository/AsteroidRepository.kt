package com.udacity.asteroidradar.repository

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.database.dao.LocalAsteroidDataSource
import com.udacity.asteroidradar.database.entity.asDomain
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.asDatabase
import com.udacity.asteroidradar.network.*
import com.udacity.asteroidradar.util.getCurrentFormattedDate
import com.udacity.asteroidradar.util.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

interface AsteroidRepository {
    val asteroids: MediatorLiveData<List<Asteroid>>
    suspend fun refreshAsteroids(startDate: String?, endDate: String?)
    suspend fun filterAsteroids(filter: NEoWsAsteroidFilter)
    suspend fun deletePreviousAsteroids(endDate: String)
}

class DefaultAsteroidRepository(
    private val remoteAsteroidDataSource: RemoteAsteroidDataSource,
    private val localAsteroidDataSource: LocalAsteroidDataSource
) : AsteroidRepository {

    private val filteredAsteroids = MutableLiveData<List<Asteroid>?>()

    override val asteroids = MediatorLiveData<List<Asteroid>>().apply {
        addSource(
            localAsteroidDataSource.getAllSortedAsteroidsFromToday()
        ) {
            this.value = it?.asDomain() ?: listOf()
        }
        addSource(filteredAsteroids) {
            this.value = it ?: listOf()
        }

    }

    override suspend fun refreshAsteroids(startDate: String?, endDate: String?) {
        withContext(Dispatchers.IO) {
            val jsonString =
                remoteAsteroidDataSource.getAsteroids(startDate = startDate, endDate = endDate)
            val list = parseAsteroidsJsonResult(JSONObject(jsonString))
            localAsteroidDataSource.insertAll(list.asDatabase())
        }
    }

    override suspend fun filterAsteroids(filter: NEoWsAsteroidFilter) {
        withContext(Dispatchers.IO) {
            val list = when (filter) {
                NEoWsAsteroidFilter.SHOW_TODAY -> {
                    localAsteroidDataSource.getSortedAsteroidsWithFilter(
                        getCurrentFormattedDate(),
                        getCurrentFormattedDate()
                    )
                }
                NEoWsAsteroidFilter.SHOW_WEEKLY -> {
                    localAsteroidDataSource.getSortedAsteroidsWithFilter(
                        getCurrentFormattedDate(),
                        getCurrentFormattedDate(7)
                    )
                }
                NEoWsAsteroidFilter.SHOW_ALL -> {
                    localAsteroidDataSource.getAllSortedAsteroids()
                }
            }
            withContext(Dispatchers.Main) {
                filteredAsteroids.value = list?.asDomain()
            }
        }
    }

    override suspend fun deletePreviousAsteroids(endDate: String) {
        withContext(Dispatchers.IO) {
            localAsteroidDataSource.deletePreviousAsteroids(endDate)
        }
    }
}

enum class NEoWsAsteroidFilter {
    SHOW_TODAY, SHOW_WEEKLY, SHOW_ALL
}

sealed class NEoWsApiStatus {
    object Loading : NEoWsApiStatus()
    object Success : NEoWsApiStatus()
    data class Failure(val errorMsg: String) : NEoWsApiStatus()
}

