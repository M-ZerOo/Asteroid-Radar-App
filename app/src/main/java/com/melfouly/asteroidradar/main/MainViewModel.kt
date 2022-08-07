package com.melfouly.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.melfouly.asteroidradar.Constants.TAG
import com.melfouly.asteroidradar.database.getDatabase
import com.melfouly.asteroidradar.model.Asteroid
import com.melfouly.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)

    private val _detailedAsteroid = MutableLiveData<Asteroid?>()
    val detailedAsteroid: LiveData<Asteroid?> get() = _detailedAsteroid

    init {
        viewModelScope.launch {
            try {
                repository.refreshPictureOfDay()
                repository.refreshAsteroids()
            } catch (e: Exception) {
                Log.d(TAG, "e:${e.message}")
            }
        }
    }

    var allAsteroids = repository.asteroids
    var pictureOfDay = repository.pictureOfDay
    var todayOnlyAsteroids = repository.todayOnlyAsteroids


    // Save clicked asteroid from RecyclerView to observe later
    fun onAsteroidClicked(asteroid: Asteroid) {
        _detailedAsteroid.value = asteroid
    }

    // Event for Navigating to Details Fragment Successfully and make the values null again
    fun doneNavigating() {
        _detailedAsteroid.value = null
    }

}
