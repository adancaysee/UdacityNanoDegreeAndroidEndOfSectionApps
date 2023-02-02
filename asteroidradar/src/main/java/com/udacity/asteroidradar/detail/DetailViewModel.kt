package com.udacity.asteroidradar.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.udacity.asteroidradar.domain.Asteroid

class DetailViewModel(selectedAsteroid: Asteroid) : ViewModel() {

    private val _asteroid = MutableLiveData<Asteroid>()
    val asteroid: LiveData<Asteroid>
        get() = _asteroid

    private val _displayAstronomicalUnitExplanationEvent = MutableLiveData<Boolean>()
    val displayAstronomicalUnitExplanationEvent: LiveData<Boolean>
        get() = _displayAstronomicalUnitExplanationEvent


    init {
        _asteroid.value = selectedAsteroid
    }

    fun onDisplayAstronomicalUnitExplanation() {
        _displayAstronomicalUnitExplanationEvent.value = true
    }

    fun doneDisplayAstronomicalUnitExplanation() {
        _displayAstronomicalUnitExplanationEvent.value = false
    }

    companion object {
        fun createFactory(asteroid: Asteroid): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    DetailViewModel(asteroid)
                }
            }
        }
    }
}