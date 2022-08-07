package com.melfouly.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.melfouly.asteroidradar.Constants.TAG
import com.melfouly.asteroidradar.database.AsteroidDatabase
import com.melfouly.asteroidradar.database.asDatabaseModel
import com.melfouly.asteroidradar.database.asDomainModel
import com.melfouly.asteroidradar.model.Asteroid
import com.melfouly.asteroidradar.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    // Call all asteroids from database and set the data as domain model
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(
            database.asteroidDao().getAsteroidsOfToday(today(), sevenDaysAfter())
        ) { it.asDomainModel() }

    // Call picture of day from database and  set the data as domain model
    val pictureOfDay: LiveData<PictureOfDay> =
        Transformations.map(
            database.pictureOfDayDao().getPictureOfDay()
        ) { it?.asDomainModel() }

    // Call today only asteroids from database and set the data as domain model
    val todayOnlyAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(
            database.asteroidDao().getAsteroidsOfToday(today(), today())
        ) { it.asDomainModel() }


    // Call the asteroids from network then delete previous days and insert the data to database
    suspend fun refreshAsteroids(startDate: String = today(), endDate: String = sevenDaysAfter()) {
        var asteroidList: ArrayList<Asteroid>

        withContext(Dispatchers.IO) {
            try {
                val response =
                    Network.asteroid.getAllAsteroidsAsync(startDate, endDate)
                val jsonObject = JSONObject(response)
                asteroidList = parseAsteroidsJsonResult(jsonObject)
                database.asteroidDao().deletePreviousDay(today())
                database.asteroidDao().insertAll(*asteroidList.asDatabaseModel())
            } catch (e: Exception) {
                Log.d(TAG, "Error: ${e.localizedMessage}")
            }
        }
    }

    // Get picture of day from network and check if it's photo or video and save the image to database
    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                val pictureOfDay = Network.asteroid.getPictureOfDayAsync().await()
                if (pictureOfDay.mediaType == "image") {
                    database.pictureOfDayDao().insertImage(pictureOfDay.asDatabaseModel())
                } else return@withContext
            } catch (e: Exception) {
                Log.d(TAG, "Error: ${e.localizedMessage}")
            }
        }
    }

}