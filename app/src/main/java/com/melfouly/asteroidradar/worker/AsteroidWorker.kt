package com.melfouly.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.melfouly.asteroidradar.database.getDatabase
import com.melfouly.asteroidradar.network.sevenDaysAfter
import com.melfouly.asteroidradar.network.today
import com.melfouly.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class AsteroidWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)

        /* Refresh the data from network and save it to database, also refreshes the picture of
         the day from network and save it to database */
        return try {
            repository.refreshAsteroids(today(), sevenDaysAfter())
            repository.refreshPictureOfDay()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}