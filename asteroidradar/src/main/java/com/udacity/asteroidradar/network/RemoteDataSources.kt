package com.udacity.asteroidradar.network

import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteAsteroidDataSource {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String?,
        @Query("end_date") endDate: String?,
    ): String
}

interface RemotePictureOfDayDataSource {
    @GET("planetary/apod")
    suspend fun getPictureOfDay(): NetworkPictureOfDay
}