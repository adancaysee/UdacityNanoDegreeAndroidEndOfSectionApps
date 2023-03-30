package com.udacity.project4.geofence

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.project4.data.source.repository.ReminderRepository
import org.koin.java.KoinJavaComponent.inject


class GeofenceWorker(private val appContext: Context, param: WorkerParameters) :
    CoroutineWorker(appContext, param) {

    companion object {
        const val WORK_NAME = "GeofenceDataWorker"
    }

    override suspend fun doWork(): Result {
        val repository: ReminderRepository by inject(ReminderRepository::class.java)
        val triggeringGeofences = inputData.getStringArray("geofences")
        triggeringGeofences?.forEach { id ->
            val result = repository.getReminder(id)
            if (result is com.udacity.project4.data.source.repository.Result.Success) {
                val reminder = result.data
                sendReminderNotification(appContext, reminder)
            }
        }
        return Result.success()
    }


}