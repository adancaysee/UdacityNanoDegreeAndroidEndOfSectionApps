package com.udacity.locationreminder.domain.savereminder

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import com.udacity.locationreminder.base.BaseViewModel
import com.udacity.locationreminder.data.source.repository.ReminderRepository
import com.udacity.locationreminder.data.domain.Reminder
import kotlinx.coroutines.launch
import com.udacity.locationreminder.R
import com.udacity.locationreminder.base.NavigationCommand
import com.udacity.locationreminder.data.domain.Address
import com.udacity.locationreminder.geofence.GeofenceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

class SaveReminderViewModel(
    private val application: Application,
    private val reminderRepository: ReminderRepository
) : BaseViewModel(application) {

    private val _reminder = MutableLiveData(Reminder())
    val reminder: LiveData<Reminder> = _reminder

    private val _selectedAddress = MutableLiveData<Address?>()
    val selectedAddress: LiveData<Address?> = _selectedAddress

    private val _currentAddress = MutableLiveData<Address?>()
    val currentAddress: LiveData<Address?> = _currentAddress

    val isSaveActive: LiveData<Boolean> = Transformations.map(_reminder) {
        it?.latitude != 0.0 && it?.longitude != 0.0
    }

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    private var locationCallback: LocationCallback? = null
    private var geofencePendingIntent: PendingIntent = GeofenceHelper.getPendingIntent(application)
    private var geofencingClient: GeofencingClient =
        LocationServices.getGeofencingClient(application)

    @SuppressLint("MissingPermission")
    fun initFusedLocationClient() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    setCurrentAddress(Address(location = it))
                    fusedLocationClient.removeLocationUpdates(this)
                    locationCallback = null
                }
            }
        }
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0)
            .setWaitForAccurateLocation(false)
            .build()
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    fun getGeoCodeLocation(location: Location?) {
        showLoading.value = true
        if (location == null) return
        val geocoder = Geocoder(application, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                geocoder.getFromLocation(location.latitude, location.longitude, 1) { list ->
                    val address = list.map {
                        Address(
                            it.thoroughfare,
                            it.subThoroughfare,
                            it.locality,
                            it.adminArea,
                            it.postalCode,
                            location,
                        )
                    }.first()
                    setSelectedAddress(address)
                }
            } catch (ex: Exception) {
                setSelectedAddress(Address(location = location))
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
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
                                it.postalCode,
                                location,
                            )
                        }?.first()
                        setSelectedAddress(address)
                    } catch (ex: Exception) {
                        setSelectedAddress(Address(location = location))
                    }
                }
            }
        }
    }

    private fun setSelectedAddress(address: Address?) = viewModelScope.launch {
        address?.let {
            showLoading.value = false
            _selectedAddress.value = it
            _reminder.value =
                _reminder.value?.copy(
                    latitude = it.location.latitude,
                    longitude = it.location.longitude,
                    locationSnippet = it.getSnippet()
                )
        }
    }

    private fun setCurrentAddress(address: Address?) = viewModelScope.launch {
        address?.let { _currentAddress.value = it }
    }

    fun clearGeofence() {
        _reminder.value = _reminder.value?.copy(latitude = 0.0, longitude = 0.0)
    }

    fun saveLocation() {
        navigationCommandEvent.value = NavigationCommand.Back
    }

    fun validateAndSaveReminder() {
        val reminder = _reminder.value
        if (reminder != null && validateEnteredData(reminder))
            if (validateEnteredData(reminder)) {
                saveReminder(reminder)
            }
    }

    private fun saveReminder(reminder: Reminder) {
        showLoading.value = true
        viewModelScope.launch {
            reminderRepository.saveReminder(reminder)
            addGeofence(reminder)
            clearData()
            showLoading.value = false
            showToastEvent.value = application.getString(R.string.reminder_saved)
            navigationCommandEvent.value = NavigationCommand.Back
        }
    }

    @SuppressLint("MissingPermission")
    private fun addGeofence(reminder: Reminder) {
        if (reminder.latLng == null) return
        val geofence = GeofenceHelper.getGeofence(reminder.id, reminder.latLng)
        val geofenceRequest = GeofenceHelper.getGeofencingRequest(geofence)

        geofencingClient.addGeofences(geofenceRequest, geofencePendingIntent).run {
            addOnCompleteListener {
                Timber.d("succeed")
            }
        }
    }

    fun validateEnteredData(reminder: Reminder): Boolean {
        if (reminder.title.isNullOrEmpty()) {
            showSnackBarIntEvent.value = R.string.err_enter_title
            return false
        }
        if (reminder.latitude == 0.0) {
            showSnackBarIntEvent.value = R.string.err_select_location
            return false
        }
        return true
    }

    override fun onCleared() {
        super.onCleared()
        clearData()
    }

    fun clearData() {
        _selectedAddress.value = null
        _currentAddress.value = null
        _reminder.value = Reminder()
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback!!)
            locationCallback = null
        }
    }
}