package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.network.NEoWsApi

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            val remoteAsteroidDataSource = NEoWsApi.remoteAsteroidDataSource
            val localAsteroidDataSource = AsteroidRadarDatabase.getInstance(applicationContext).localAsteroidDataSource



            Result.success()
        }catch (ex:Exception) {
            Result.failure()
        }

    }

}