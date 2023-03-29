package com.udacity.locationreminder.geofence

import android.content.Context
import com.google.android.gms.location.GeofenceStatusCodes
import com.udacity.locationreminder.R

object GeofencingConstants {
    const val GEOFENCE_REQUEST_CODE = 0
    const val ACTION_GEOFENCE_EVENT = "treasureHunt.action.ACTION_GEOFENCE_EVENT"
    const val GEOFENCE_RADIUS_IN_METERS = 100f
}

fun errorMessage(context: Context, errorCode: Int?): String {
    val resources = context.resources
    return when (errorCode) {
        GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> resources.getString(
            R.string.geofence_not_available
        )
        GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> resources.getString(
            R.string.geofence_too_many_geofences
        )
        GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> resources.getString(
            R.string.geofence_too_many_pending_intents
        )
        else -> resources.getString(R.string.error_happened)
    }
}