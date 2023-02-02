package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.network.PictureOfDay
import com.udacity.asteroidradar.network.RemotePictureOfDayDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


interface PictureOfDayRepository {
    suspend fun getPictureOfDay(): PictureOfDay
}

class DefaultPictureOfDayRepository(
    private val remotePictureOfDayDataSource: RemotePictureOfDayDataSource,
) : PictureOfDayRepository {

    override suspend fun getPictureOfDay(): PictureOfDay {
        return withContext(Dispatchers.IO) {
            remotePictureOfDayDataSource.getPictureOfDay()
        }
    }
}