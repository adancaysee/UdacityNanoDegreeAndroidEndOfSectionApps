package com.udacity.locationreminder.data.domain

import com.udacity.locationreminder.data.source.local.ReminderEntity
import java.util.*

data class Reminder(
    var title: String?,
    var description: String?,
    var location: String?,
    var latitude: Double?,
    var longitude: Double?,
    val id: String = UUID.randomUUID().toString()
)

fun Reminder.asDatabase(): ReminderEntity = ReminderEntity(
    title,
    description,
    location,
    latitude,
    longitude,
    id
)
