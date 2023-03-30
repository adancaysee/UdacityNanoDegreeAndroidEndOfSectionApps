package com.udacity.project4.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.work.*
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) {
            return
        }
        if (intent.action == GeofenceHelper.ACTION_GEOFENCE_EVENT) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)
            if (geofencingEvent == null || geofencingEvent.hasError()) {
                return
            }
            Toast.makeText(context,"triggered",Toast.LENGTH_LONG).show()
            if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                geofencingEvent.triggeringGeofences?.let {
                    runWork(context,it)
                }
            }
        }

    }
    private fun runWork(context: Context, geofences: MutableList<Geofence>) {
        val list = geofences.map { it.requestId }
        val data = Data.Builder()
        data.putStringArray("geofences", list.toTypedArray())


        val request = OneTimeWorkRequestBuilder<GeofenceWorker>()
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            GeofenceWorker.WORK_NAME,
            ExistingWorkPolicy.KEEP,
            request
        )

    }
}