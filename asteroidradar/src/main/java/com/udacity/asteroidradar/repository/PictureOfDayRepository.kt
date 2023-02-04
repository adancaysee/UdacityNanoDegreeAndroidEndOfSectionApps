package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.database.dao.LocalPictureOfDayDataSource
import com.udacity.asteroidradar.database.entity.asDomain
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.RemotePictureOfDayDataSource
import com.udacity.asteroidradar.network.asDatabase
import com.udacity.asteroidradar.network.asDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


interface PictureOfDayRepository {
    suspend fun getPictureOfDay(): PictureOfDay?
}

class DefaultPictureOfDayRepository(
    private val remotePictureOfDayDataSource: RemotePictureOfDayDataSource,
    private val localPictureOfDayDataSource: LocalPictureOfDayDataSource
) : PictureOfDayRepository {

    override suspend fun getPictureOfDay(): PictureOfDay? {
        return withContext(Dispatchers.IO) {
            try {
                val picture = remotePictureOfDayDataSource.getPictureOfDay()
                if (picture.mediaType == "image") {
                    localPictureOfDayDataSource.insert(picture.asDatabase())
                    picture.asDomain()
                }else {
                    null
                }
            }catch (ex:Exception) {
                val picture = localPictureOfDayDataSource.getPictureOfDay()
                picture?.asDomain()
            }
        }
    }
}