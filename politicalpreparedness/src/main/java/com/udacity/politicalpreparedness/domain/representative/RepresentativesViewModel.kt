package com.udacity.politicalpreparedness.domain.representative

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.politicalpreparedness.data.domain.Representative
import com.udacity.politicalpreparedness.data.repository.Result
import com.udacity.politicalpreparedness.data.repository.RepresentativeRepository
import kotlinx.coroutines.launch
import com.google.android.gms.location.*
import com.udacity.politicalpreparedness.util.SingleLiveEvent


class RepresentativesViewModel(
    private val application: Application,
    private val representativeRepository: RepresentativeRepository
) : AndroidViewModel(application) {

    private val _representativeList = MutableLiveData<List<Representative>>()

    private val _dataLoading = MutableLiveData(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _checkLocationSettingFailureEvent = SingleLiveEvent<Exception?>()
    val checkLocationSettingFailureEvent: SingleLiveEvent<Exception?>
        get() = _checkLocationSettingFailureEvent

    init {
        _dataLoading.value = true
        viewModelScope.launch {
            val result = representativeRepository.fetchRepresentatives()
            if (result is Result.Success) {
                val response = result.data
                _representativeList.value = response
            }
        }
        _dataLoading.value = false
    }

    fun checkDeviceLocationSettingsAndFindMyLocation() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_LOW_POWER, 10000)
            .build()

        val locationSettingRequest =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                .build()

        val settingClient = LocationServices.getSettingsClient(application)
        val locationSettingResponseTask =
            settingClient.checkLocationSettings(locationSettingRequest)

        locationSettingResponseTask.addOnCompleteListener {
            if (it.isSuccessful) findMyLocation()
        }.addOnFailureListener { exception ->
            _checkLocationSettingFailureEvent.value = exception

        }
    }


    fun findMyLocation() {

    }
}