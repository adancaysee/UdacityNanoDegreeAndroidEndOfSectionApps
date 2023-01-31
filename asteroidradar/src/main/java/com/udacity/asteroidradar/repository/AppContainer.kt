package com.udacity.asteroidradar.repository

import android.content.Context
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.network.NEoWsApi

interface AppContainer {
    val repository: AsteroidRepository
}

class DefaultAppContainer(context: Context) : AppContainer {

    override val repository: AsteroidRepository by lazy {
        DefaultAsteroidRepository(
            NEoWsApi.remoteAsteroidDataSource,
            NEoWsApi.remotePictureOfDayDataSource,
            AsteroidRadarDatabase.getInstance(context).localAsteroidDataSource
        )
    }

}