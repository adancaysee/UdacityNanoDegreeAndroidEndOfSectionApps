package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.util.getCurrentFormattedDate

class RefreshDataWorker(
    appContext: Context,
    params: WorkerParameters,
    private val repository: AsteroidRepository
) : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            //fetch today's asteroid
            repository.refreshAsteroids(
                startDate = getCurrentFormattedDate(),
                endDate = getCurrentFormattedDate()
            )
            //delete the asteroids before today
            repository.deletePreviousAsteroids(getCurrentFormattedDate())
            Result.success()
        } catch (ex: Exception) {
            Result.failure()
        }
    }

    class Factory(private val repository: AsteroidRepository) : WorkerFactory() {
        override fun createWorker(
            appContext: Context, workerClassName: String, workerParameters: WorkerParameters
        ): ListenableWorker {
            return RefreshDataWorker(appContext, workerParameters, repository)
        }
    }

}