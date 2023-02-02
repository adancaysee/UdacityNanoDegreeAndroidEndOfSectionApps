package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.domain.Asteroid

interface AsteroidRepository {
    val asteroids: LiveData<List<Asteroid>>
    suspend fun fetchAsteroids()
}


