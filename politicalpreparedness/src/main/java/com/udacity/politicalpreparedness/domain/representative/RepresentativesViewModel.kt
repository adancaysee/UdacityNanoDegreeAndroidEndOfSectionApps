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
import com.udacity.politicalpreparedness.data.repository.Result


class RepresentativesViewModel(
    private val application: Application,
    private val representativeRepository: RepresentativeRepository
) : AndroidViewModel(application) {

    private val _representativeList = MutableLiveData<List<Representative>>()
    val representativeList = _representativeList

    private val _dataLoading = MutableLiveData(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _enabled = MutableLiveData(true)
    val enabled: LiveData<Boolean> = _enabled

    val userAddress = MutableLiveData(Address("", "", "", "", ""))
    val states: Array<String> = application.resources.getStringArray(R.array.states)

    private val _selectedStatePosition = MutableLiveData<Int>()
    val selectedStatePosition: LiveData<Int> = _selectedStatePosition

    private val _checkLocationSettingFailureEvent = SingleLiveEvent<Exception?>()
    val checkLocationSettingFailureEvent: SingleLiveEvent<Exception?> =
        _checkLocationSettingFailureEvent
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    private var locationCallback: LocationCallback? = null

    private val _snackBarMessageEvent = SingleLiveEvent<String>()
    val snackBarMessageEvent = _snackBarMessageEvent

    fun getRepresentatives() {
        _dataLoading.value = true
        viewModelScope.launch {
            val address = userAddress.value
            if (address != null) {
                val result =
                    representativeRepository.fetchRepresentatives(address.toFormattedString())
                if (result is Result.Success) {
                    val response = result.data
                    _representativeList.value = response
                }else {
                    _snackBarMessageEvent.value = "Not found,please enter your location and retry"
                }
            } else {
                _snackBarMessageEvent.value = application.getString(R.string.location_not_found)
            }
            _dataLoading.value = false
            _enabled.value = true
        }
    }

    fun getLocation() {
        _enabled.value = false
        _dataLoading.value = true
        _selectedStatePosition.value = 0
        userAddress.value = null
        initFusedLocationClient()
    }

    @SuppressLint("MissingPermission")
    private fun initFusedLocationClient() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.let {
                    geoCodeLocation(it.lastLocation)
                    fusedLocationClient.removeLocationUpdates(this)
                    locationCallback = null
                }
            }
        }
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0)
            .setWaitForAccurateLocation(false)
            .build()
        Looper.myLooper()?.let {
            fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallback!!,
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

    fun setSelectedState(pos: Int) {
        _selectedStatePosition.value = pos
    }

    private fun setAddress(address: Address?) {
        viewModelScope.launch {
            if (address != null) {
                val index = states.indexOf(address.state ?: "")
                if (index >= 0) {
                    // Set the selected index value
                    _selectedStatePosition.value = index
                    userAddress.value = address
                    getRepresentatives()
                } else {
                    _snackBarMessageEvent.value = application.getString(R.string.location_error)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback!!)
        }

    }
}
