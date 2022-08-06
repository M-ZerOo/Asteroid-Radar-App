package com.melfouly.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.melfouly.asteroidradar.Constants.TAG
import com.melfouly.asteroidradar.database.AsteroidDatabase
import com.melfouly.asteroidradar.database.AsteroidEntity
import com.melfouly.asteroidradar.database.asDatabaseModel
import com.melfouly.asteroidradar.database.asDomainModel
import com.melfouly.asteroidradar.model.Asteroid
import com.melfouly.asteroidradar.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    // Call from database and set the data to domain model
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(
            database.asteroidDao().getAsteroidsOfToday(today(), sevenDaysAfter())
        ) { it.asDomainModel() }

    val todayOnlyAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(
            database.asteroidDao().getAsteroidsOfToday(today(), today())
        ) { it.asDomainModel() }



    // Refresh Asteroids saved in offline cache
    suspend fun refreshAsteroids(startDate: String = today(), endDate: String = sevenDaysAfter()) {
        var asteroidList: ArrayList<Asteroid>

        withContext(Dispatchers.IO) {
            try {
                val response =
                    Network.asteroid.getAllAsteroidsAsync(startDate, endDate)
                val jsonObject = JSONObject(response)
                asteroidList = parseAsteroidsJsonResult(jsonObject)
                database.asteroidDao().insertAll(*asteroidList.asDatabaseModel())
            } catch (e: Exception) {
                Log.d(TAG, "Error: ${e.localizedMessage}")
            }
        }
    }

    // Get picture of day from network and check if it's photo or video
    suspend fun getPictureOfDay(): PictureOfDay? {
        var pictureOfDay: PictureOfDay
        withContext(Dispatchers.IO) {
            pictureOfDay = Network.asteroid.getPictureOfDayAsync().await()
        }
        if (pictureOfDay.mediaType == "image") {
            return pictureOfDay
        }
        return null
    }

    suspend fun getSavedAsteroids() {
        var asteroids: LiveData<List<AsteroidEntity>>
        withContext(Dispatchers.IO) { try {
            asteroids = database.asteroidDao().getAll()
            Log.d(TAG, "Lol: ${asteroids.value?.size}")
        } catch (e: Exception) {
            Log.d(TAG, "Error is : ${e.localizedMessage}")
        }
        }
    }

}