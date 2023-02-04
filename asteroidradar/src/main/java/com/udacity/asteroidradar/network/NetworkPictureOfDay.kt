package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.udacity.asteroidradar.database.EntityPictureOfDay
import com.udacity.asteroidradar.domain.PictureOfDay

data class NetworkPictureOfDay(
    @Json(name = "media_type") val mediaType: String,
    val title: String,
    val url: String,
)

fun NetworkPictureOfDay.asDatabase(): EntityPictureOfDay {
    return EntityPictureOfDay(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url,
    )
}

fun NetworkPictureOfDay.asDomain(): PictureOfDay {
    return PictureOfDay(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url,
    )
}