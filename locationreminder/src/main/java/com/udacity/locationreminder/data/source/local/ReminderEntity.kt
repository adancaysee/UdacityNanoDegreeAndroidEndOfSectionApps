package com.udacity.locationreminder.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.locationreminder.data.domain.Reminder
import java.util.*

@Entity(tableName = "reminders_table")
data class ReminderEntity(
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "location") var location: String?,
    @ColumnInfo(name = "latitude") var latitude: Double?,
    @ColumnInfo(name = "longitude") var longitude: Double?,
    @PrimaryKey @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString()
)

fun ReminderEntity.asDomain(): Reminder = Reminder(
        title,
        description,
        location,
        latitude,
        longitude,
        id
    )

fun List<ReminderEntity>.asDomain(): List<Reminder> = map {
    Reminder(
        it.title,
        it.description,
        it.location,
        it.latitude,
        it.longitude,
        it.id
    )
}