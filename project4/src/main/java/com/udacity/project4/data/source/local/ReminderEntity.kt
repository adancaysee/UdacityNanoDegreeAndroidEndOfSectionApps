package com.udacity.project4.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.project4.data.domain.Reminder
import java.util.*

@Entity(tableName = "reminders_table")
data class ReminderEntity(
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "latitude") var latitude: Double,
    @ColumnInfo(name = "longitude") var longitude: Double,
    @ColumnInfo(name = "location_snippet") var locationSnippet: String?,
    @PrimaryKey @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString()
)

fun ReminderEntity.asDomain(): Reminder = Reminder(
    title,
    description,
    latitude,
    longitude,
    locationSnippet,
    id
)

fun List<ReminderEntity>.asDomain(): List<Reminder> = map {
    Reminder(
        it.title,
        it.description,
        it.latitude,
        it.longitude,
        it.locationSnippet,
        it.id
    )
}