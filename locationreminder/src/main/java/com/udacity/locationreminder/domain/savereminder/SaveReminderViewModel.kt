package com.udacity.locationreminder.domain.savereminder

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import com.udacity.locationreminder.base.BaseViewModel
import com.udacity.locationreminder.data.source.repository.ReminderRepository
import com.udacity.locationreminder.data.domain.Location
import com.udacity.locationreminder.data.domain.Reminder
import com.udacity.locationreminder.util.SingleLiveEvent
import kotlinx.coroutines.launch
import com.udacity.locationreminder.R
import com.udacity.locationreminder.geofence.GeofenceBroadcastReceiver
import com.udacity.locationreminder.geofence.GeofencingConstants
import com.udacity.locationreminder.geofence.GeofencingConstants.ACTION_GEOFENCE_EVENT
import com.udacity.locationreminder.geofence.GeofencingConstants.GEOFENCE_REQUEST_CODE


class SaveReminderViewModel(
    private val application: Application,
    private val reminderRepository: ReminderRepository
) : BaseViewModel(application) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    private var locationCallback: LocationCallback? = null
    private var geofencingClient: GeofencingClient
    private var geofencePendingIntent: PendingIntent

    var reminder = MutableLiveData(Reminder())

    val locationStr: LiveData<String> = Transformations.map(reminder) {
        it?.location?.let { location ->
            application.getString(R.string.lat_long_snippet, location.latitude, location.longitude)
        } ?: ""
    }
    val isSaveActive: LiveData<Boolean> = Transformations.map(reminder) {
        it?.location != null
    }

    private val _showCurrentLocationEvent = SingleLiveEvent<Location>()
    val showCurrentLocationEvent = _showCurrentLocationEvent

    private val _navigateGeocodeDetailEvent = SingleLiveEvent<Boolean>()
    val navigateGeocodeDetailEvent: LiveData<Boolean> = _navigateGeocodeDetailEvent

    private val _navigateReminderListEvent = SingleLiveEvent<Boolean>()
    val navigateReminderListEvent: LiveData<Boolean> = _navigateReminderListEvent


    init {
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        }
        val intent = Intent(application, GeofenceBroadcastReceiver::class.java)
        intent.action = ACTION_GEOFENCE_EVENT
        geofencePendingIntent = PendingIntent.getBroadcast(
            application,
            GEOFENCE_REQUEST_CODE,
            intent,
            flag
        )

        geofencingClient = LocationServices.getGeofencingClient(application)
    }

    fun getCurrentLocation() {
        initFusedLocationClient()
    }

    @SuppressLint("MissingPermission")
    private fun initFusedLocationClient() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    showCurrentLocation(Location(it.latitude, it.longitude))
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

    private fun showCurrentLocation(latLng: Location) {
        viewModelScope.launch {
            _showCurrentLocationEvent.value = latLng
        }
    }

    fun getGeocodeLocationInfo(location: Location) {
        reminder.value = reminder.value?.copy(location = location)
    }

    fun clearGeofence() {
        reminder.value = reminder.value?.copy(location = null)
    }

    fun saveLocation() {
        _navigateGeocodeDetailEvent.value = true
    }

    override fun onCleared() {
        super.onCleared()
        clearData()
    }

    fun saveReminder() {
        val reminder = reminder.value
        if (reminder != null) {
            if (reminder.location == null) {
                showErrorMessageEvent.value = "Please select a location"
                return
            } else if (reminder.title == null) {
                showErrorMessageEvent.value = "Please choose a title for your reminder"
                return
            }
        } else {
            showErrorMessageEvent.value = "Please complete required field"
            return
        }
        viewModelScope.launch {
            reminderRepository.saveReminder(reminder)
            _navigateReminderListEvent.value = true
            clearData()
        }
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
            _navigateReminderListEvent.value = true
        }
    }

    private fun validateEnteredData(reminder:Reminder): Boolean {
        if (reminder.title.isNullOrEmpty()) {
            showSnackBarIntEvent.value = R.string.err_enter_title
            return false
        }

        if (reminder.location == null) {
            showSnackBarIntEvent.value = R.string.err_select_location
            return false
        }
        return true
    }

    @SuppressLint("MissingPermission")
    fun addGeofence(location: Location) {
        val geofence = Geofence.Builder().apply {
            setRequestId("")
            setCircularRegion(
                location.latitude,
                location.longitude,
                GeofencingConstants.GEOFENCE_RADIUS_IN_METERS
            )
            setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
        }.build()

        val geofenceRequest = GeofencingRequest.Builder().apply {
            addGeofence(geofence)
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        }.build()

        geofencingClient.removeGeofences(geofencePendingIntent).run {
            addOnCompleteListener {
                geofencingClient.addGeofences(geofenceRequest, geofencePendingIntent).run {
                    addOnCompleteListener {

                    }
                }
            }
        }
    }

    fun removeGeofences() {
        geofencingClient.removeGeofences(geofencePendingIntent)
    }

    private fun clearData() {
        reminder.value = null
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback!!)
        }
    }
}