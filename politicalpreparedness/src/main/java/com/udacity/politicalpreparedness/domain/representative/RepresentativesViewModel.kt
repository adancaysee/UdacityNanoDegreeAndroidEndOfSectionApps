package com.udacity.politicalpreparedness.domain.representative

import android.annotation.SuppressLint
import android.app.Application
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.lifecycle.*
import com.google.android.gms.location.*
import com.udacity.politicalpreparedness.data.domain.Representative
import com.udacity.politicalpreparedness.data.repository.RepresentativeRepository
import com.udacity.politicalpreparedness.data.domain.Address
import com.udacity.politicalpreparedness.R
import com.udacity.politicalpreparedness.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*



class RepresentativesViewModel(
    private val application: Application,
    private val representativeRepository: RepresentativeRepository
) : AndroidViewModel(application) {

    private val _representativeList = MutableLiveData<List<Representative>>()

    private val _dataLoading = MutableLiveData(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _enabled = MutableLiveData(true)
    val enabled: LiveData<Boolean> = _enabled

    val userAddress = MutableLiveData(Address("","","","",""))
    val states:Array<String> = application.resources.getStringArray(R.array.states)

    private val _selectedStatePosition = MutableLiveData<Int>()
    val selectedStatePosition: LiveData<Int> = _selectedStatePosition

    private val _checkLocationSettingFailureEvent = SingleLiveEvent<Exception?>()
    val checkLocationSettingFailureEvent: SingleLiveEvent<Exception?> =  _checkLocationSettingFailureEvent
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)


    fun getLocation() {
        _enabled.value = false
        _dataLoading.value = true
        initFusedLocationClient()
    }

    @SuppressLint("MissingPermission")
    private fun initFusedLocationClient() {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.let {
                    geoCodeLocation(it.lastLocation)
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }
        }
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0)
            .setWaitForAccurateLocation(false)
            .build()
        Looper.myLooper()?.let {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,
                it
            )
        }
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
            if (it.isSuccessful) getLocation()
        }.addOnFailureListener { exception ->
            _checkLocationSettingFailureEvent.value = exception
        }
    }

    private fun geoCodeLocation(location: Location?) {
        if (location == null) return
        val geocoder = Geocoder(application, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(location.latitude, location.longitude, 1) { list ->
                val address = list.map {
                    Address(
                        it.thoroughfare,
                        it.subThoroughfare,
                        it.locality,
                        it.adminArea,
                        it.postalCode
                    )
                }.first()
                setAddress(address)
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    @Suppress("DEPRECATION")
                    val address = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )?.map {
                        Address(
                            it.thoroughfare,
                            it.subThoroughfare,
                            it.locality,
                            it.adminArea,
                            it.postalCode
                        )
                    }?.first()
                    setAddress(address)
                }
            }

        }

    }

    fun setSelectedState(pos:Int) {
        _selectedStatePosition.value = pos
    }

    private fun setAddress(address: Address?) {
        viewModelScope.launch {
            if (address != null) {
                userAddress.value = address
            }
            _dataLoading.value = false
            _enabled.value = true
        }
    }
}

/*
_dataLoading.value = true
        viewModelScope.launch {
            val result = representativeRepository.fetchRepresentatives()
            if (result is Result.Success) {
                val response = result.data
                _representativeList.value = response
            }
        }
        _dataLoading.value = false
 */