package com.udacity.asteroidradar.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.PictureOfDay

@Entity(tableName = "picture_of_day_table")
data class EntityPictureOfDay(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    val mediaType: String,
    val title: String,
    val url: String,
)

fun EntityPictureOfDay.asDomain(): PictureOfDay {
    return PictureOfDay(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}