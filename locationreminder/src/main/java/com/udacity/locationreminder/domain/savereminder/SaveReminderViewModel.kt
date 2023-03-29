package com.udacity.locationreminder.domain.savereminder

import android.annotation.SuppressLint
import android.app.Application
import android.location.Geocoder
import android.location.Location
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
import kotlinx.coroutines.Dispatchers
import java.util.*

class SaveReminderViewModel(
    private val application: Application,
    private val reminderRepository: ReminderRepository
) : BaseViewModel(application) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    private var locationCallback: LocationCallback? = null

    var reminder = MutableLiveData(Reminder())

    private val _selectedAddress = MutableLiveData<Address?>()
    val selectedAddress:LiveData<Address?> = _selectedAddress

    val isSaveActive: LiveData<Boolean> = Transformations.map(reminder) {
        it?.latitude != 0.0 && it?.longitude != 0.0
    }

    @SuppressLint("MissingPermission")
    fun initFusedLocationClient() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    getGeoCodeLocation(it)
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

    fun getGeoCodeLocation(location: Location) {
        val geocoder = Geocoder(application, Locale.getDefault())
        viewModelScope.launch(Dispatchers.IO) {
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
            setAddress(address)
        }
    }

    private fun setAddress(address: Address?) {
        viewModelScope.launch {
            address?.let {
                _selectedAddress.value = it
                reminder.value =
                    reminder.value?.copy(
                        latitude = it.location.latitude,
                        longitude = it.location.longitude,
                        locationSnippet = it.getSnippet()
                    )
            }
        }
    }

    fun clearGeofence() {
        reminder.value = reminder.value?.copy(latitude = 0.0, longitude = 0.0)
    }

    fun saveLocation() {
        navigationCommandEvent.value = NavigationCommand.Back
    }
    fun validateAndSaveReminder() {
        val reminder = reminder.value
        if (reminder != null && validateEnteredData(reminder))
            if (validateEnteredData(reminder)) {
                saveReminder(reminder)
            }
    }

    private fun saveReminder(reminder: Reminder) {
        showLoading.value = true
        viewModelScope.launch {
            reminderRepository.saveReminder(reminder)
            clearData()
            showLoading.value = false
            showToastEvent.value = application.getString(R.string.reminder_saved)
            navigationCommandEvent.value = NavigationCommand.Back
        }
    }

    private fun validateEnteredData(reminder: Reminder): Boolean {
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

    private fun clearData() {
        _selectedAddress.value = null
        reminder.value = null
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback!!)
            locationCallback = null
        }
    }
}