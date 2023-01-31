package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.PictureOfDay

interface AsteroidRepository {
    val asteroids: LiveData<List<Asteroid>>
    suspend fun fetchAsteroids()
    suspend fun getPictureOfDay(): PictureOfDay
}


