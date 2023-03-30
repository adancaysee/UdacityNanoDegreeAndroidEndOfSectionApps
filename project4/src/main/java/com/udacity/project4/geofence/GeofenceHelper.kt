package com.udacity.project4.geofence

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng
import com.udacity.project4.R
import java.util.concurrent.TimeUnit

object GeofenceHelper {

    private const val GEOFENCE_REQUEST_CODE = 0
    const val ACTION_GEOFENCE_EVENT = "locationreminder.action.ACTION_GEOFENCE_EVENT"
    const val GEOFENCE_RADIUS_IN_METERS = 100f
    private val GEOFENCE_EXPIRATION_IN_MILLISECONDS: Long = TimeUnit.HOURS.toMillis(1)

    fun getGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            addGeofence(geofence)
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        }.build()

    }

    fun getGeofence(
        requestId:String,
        latLng: LatLng,
        transitionTypes: Int = Geofence.GEOFENCE_TRANSITION_ENTER
    ): Geofence {
        return Geofence.Builder().apply {
            setRequestId(requestId)
            setCircularRegion(latLng.latitude, latLng.longitude, GEOFENCE_RADIUS_IN_METERS)
            setTransitionTypes(transitionTypes)
            setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
        }.build()
    }

    fun getPendingIntent(context: Context): PendingIntent {
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        }
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        intent.action = ACTION_GEOFENCE_EVENT
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            flag
        )
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
}