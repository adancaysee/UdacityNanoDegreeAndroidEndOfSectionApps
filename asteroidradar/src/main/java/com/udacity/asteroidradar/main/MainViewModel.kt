package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.udacity.asteroidradar.AsteroidRadarApplication
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.NEoWsApiStatus
import com.udacity.asteroidradar.repository.PictureOfDayRepository
import com.udacity.asteroidradar.util.getCurrentFormattedDate
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val asteroidRepository: AsteroidRepository,
    private val pictureOfDayRepository: PictureOfDayRepository
) : AndroidViewModel(application) {

    private val _navigateToDetailEvent = MutableLiveData<Asteroid?>()
    val navigateToDetailEvent: LiveData<Asteroid?>
        get() = _navigateToDetailEvent

    private val _status = MutableLiveData<NEoWsApiStatus>()
    val status: LiveData<NEoWsApiStatus>
        get() = _status

    val asteroids: LiveData<List<Asteroid>> = asteroidRepository.asteroids

    private val _pictureOfDay = MutableLiveData<PictureOfDay?>()
    val pictureOfDay: LiveData<PictureOfDay?>
        get() = _pictureOfDay

    val pictureOfDayDescription = Transformations.map(_pictureOfDay) {
        it?.let {
            application.getString(R.string.nasa_picture_of_day_content_description_format, it.title)
        } ?: run {
            application.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
        }
    }

    init {
        fetchPictureOfDay()
        fetchAsteroids()
    }

    private fun fetchPictureOfDay() {
        viewModelScope.launch {
            try {
                _pictureOfDay.value = pictureOfDayRepository.getPictureOfDay()
            } catch (_: Throwable) {

            }
        }
    }

    private fun fetchAsteroids() {
        viewModelScope.launch {
            _status.value = NEoWsApiStatus.Loading
            try {
                asteroidRepository.refreshAsteroids(
                    startDate = getCurrentFormattedDate(),
                    endDate = null
                )
                _status.value = NEoWsApiStatus.Success
            } catch (e: Throwable) {
                _status.value = NEoWsApiStatus.Failure(e.message ?: "Unknown error occurred")
            }
        }

    }

    fun onNavigateToDetail(asteroid: Asteroid) {
        _navigateToDetailEvent.value = asteroid
    }

    fun doneNavigatingToDetail() {
        _navigateToDetailEvent.value = null
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application =
                    this[APPLICATION_KEY] as AsteroidRadarApplication
                MainViewModel(
                    application,
                    application.appContainer.asteroidRepository,
                    application.appContainer.pictureOfDayRepository,
                )
            }

        }
    }
}