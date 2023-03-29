package com.udacity.locationreminder.data.domain

import com.udacity.locationreminder.data.source.local.ReminderEntity
import java.util.*

data class Reminder(
    var title: String? = null,
    var description: String? = null,
    val location: Location? = null,
    val id: String = UUID.randomUUID().toString()
)

fun Reminder.asDatabase(): ReminderEntity = ReminderEntity(
    title,
    description,
    location?.latitude ?: 0.0,
    location?.longitude ?: 0.0,
    id
)
