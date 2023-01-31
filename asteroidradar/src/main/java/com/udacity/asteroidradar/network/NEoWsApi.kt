package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.util.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitWithScalar: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()

private val retrofitWithMoshi: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()


object NEoWsApi {
    val remoteAsteroidDataSource: RemoteAsteroidDataSource by lazy {
        retrofitWithScalar.create(RemoteAsteroidDataSource::class.java)
    }

    val remotePictureOfDayDataSource: RemotePictureOfDayDataSource by lazy {
        retrofitWithMoshi.create(RemotePictureOfDayDataSource::class.java)
    }
}
