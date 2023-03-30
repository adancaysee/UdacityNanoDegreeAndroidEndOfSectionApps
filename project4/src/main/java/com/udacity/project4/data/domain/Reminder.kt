package com.udacity.project4.data.domain

import com.google.android.gms.maps.model.LatLng
import com.udacity.project4.data.source.local.ReminderEntity
import java.util.*

data class Reminder(
    var title: String? = null,
    var description: String? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var locationSnippet: String? = null,
    val id: String = UUID.randomUUID().toString()
) {

    val latLng = if (latitude == 0.0 || longitude == 0.0) {
        null
    } else {
        LatLng(latitude, longitude)
    }
}

fun Reminder.asDatabase(): ReminderEntity = ReminderEntity(
    title,
    description,
    latitude,
    longitude,
    locationSnippet,
    id
)
