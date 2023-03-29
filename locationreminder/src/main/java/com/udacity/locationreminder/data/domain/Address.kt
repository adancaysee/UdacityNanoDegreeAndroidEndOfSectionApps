package com.udacity.locationreminder.data.domain

import android.location.Location
import android.location.LocationManager
import com.google.android.gms.maps.model.LatLng

data class Address(
    var line1: String? = null,
    var line2: String? = null,
    var city: String? = null,
    var state: String? = null,
    var zip: String? = null,
    var location: Location,
) {
    fun getSnippet():String {
        if (line1 != null) return line1!!
        if (line2 != null) return line2!!
        if (state != null) return state!!
        if (city != null) return city!!
        return "-"
    }
}

fun LatLng.getLocation(): Location = Location(LocationManager.GPS_PROVIDER).apply {
    this.latitude = this@getLocation.latitude
    this.longitude = this@getLocation.longitude
}

fun Location.getLatLng(): LatLng = LatLng(latitude, longitude)
