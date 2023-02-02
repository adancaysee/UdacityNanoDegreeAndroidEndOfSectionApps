package com.udacity.asteroidradar.repository

import android.content.Context
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.network.NEoWsApi

interface AppContainer {
    val asteroidRepository: AsteroidRepository
    val pictureOfDayRepository: PictureOfDayRepository
}

class DefaultAppContainer(context: Context) : AppContainer {

    override val asteroidRepository: AsteroidRepository by lazy {
        DefaultAsteroidRepository(
            NEoWsApi.remoteAsteroidDataSource,
            AsteroidRadarDatabase.getInstance(context).localAsteroidDataSource
        )
    }
    override val pictureOfDayRepository: PictureOfDayRepository by lazy {
        DefaultPictureOfDayRepository(
            NEoWsApi.remotePictureOfDayDataSource
        )
    }

}