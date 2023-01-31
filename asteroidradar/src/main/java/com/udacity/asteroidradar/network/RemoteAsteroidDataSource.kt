package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteAsteroidDataSource {

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("api_key") apiKey: String = API_KEY,
        /*
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        */
    ):String


}

interface RemotePictureOfDayDataSource {
    @GET("planetary/apod")
    suspend fun getPictureOfDay(@Query("api_key") apiKey: String = API_KEY): PictureOfDay
}