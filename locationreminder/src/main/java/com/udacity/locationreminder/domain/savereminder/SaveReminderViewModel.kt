package com.udacity.locationreminder.domain.savereminder

import android.app.Application
import com.google.android.gms.location.*
import com.udacity.locationreminder.base.BaseViewModel
import com.udacity.locationreminder.data.source.repository.ReminderRepository
import com.udacity.locationreminder.util.SingleLiveEvent


class SaveReminderViewModel(private val application: Application, reminderRepository: ReminderRepository) :
    BaseViewModel(application) {

    private val _checkLocationSettingFailureEvent = SingleLiveEvent<Exception?>()
    val checkLocationSettingFailureEvent: SingleLiveEvent<Exception?> =
        _checkLocationSettingFailureEvent

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
            if (it.isSuccessful) todo()
        }.addOnFailureListener { exception ->
            _checkLocationSettingFailureEvent.value = exception
        }
    }

    fun todo() {

    }
}