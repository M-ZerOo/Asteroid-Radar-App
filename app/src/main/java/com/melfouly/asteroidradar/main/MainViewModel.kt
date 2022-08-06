package com.melfouly.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.melfouly.asteroidradar.Constants.TAG
import com.melfouly.asteroidradar.database.asDomainModel
import com.melfouly.asteroidradar.database.getDatabase
import com.melfouly.asteroidradar.model.Asteroid
import com.melfouly.asteroidradar.network.PictureOfDay
import com.melfouly.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

lateinit var allAsteroids: LiveData<List<Asteroid>>

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay> get() = _pictureOfDay

    private val _detailedAsteroid = MutableLiveData<Asteroid?>()
    val detailedAsteroid: LiveData<Asteroid?> get() = _detailedAsteroid

    private val _navigateToDetailsFragment = MutableLiveData<List<Asteroid>>()
    val navigateToDetailsFragment: LiveData<List<Asteroid>> get() = _navigateToDetailsFragment

    init {
        viewModelScope.launch {
            try {
                refreshPictureOfDay()
                repository.refreshAsteroids()
            } catch (e: Exception) {
                Log.d(TAG, "e:${e.message}")
            }
        }
    }

    var allAsteroids = repository.asteroids
    var todayOnlyAsteroids = repository.todayOnlyAsteroids

    private suspend fun refreshPictureOfDay() {
        _pictureOfDay.value = repository.getPictureOfDay()
    }

    // Save clicked asteroid from RecyclerView to observe later
    fun onAsteroidClicked(asteroid: Asteroid) {
        _detailedAsteroid.value = asteroid
    }

    // Event for Navigating to Details Fragment Successfully and make the values null again
    fun doneNavigating() {
        _detailedAsteroid.value = null
    }

    fun getWeeklyAsteroids() {
        viewModelScope.launch { }
    }

    fun getTodayAsteroids() {
        Log.d(TAG, "allAsteroids: ${allAsteroids.value?.size}")
        Log.d(TAG, "todayOnly: ${todayOnlyAsteroids.value?.size}")
        allAsteroids = repository.asteroids
        todayOnlyAsteroids = repository.todayOnlyAsteroids
        Log.d(TAG, "allAsteroids: ${allAsteroids.value?.size}")
        Log.d(TAG, "todayOnly: ${repository.todayOnlyAsteroids.value?.size}")
    }

    fun getSavedAsteroids() {
        viewModelScope.launch {
//            allAsteroids =
//                Transformations.map(database.asteroidDao().getAll()) { it.asDomainModel() }
//            allAsteroids = Transformations.map(repository.getSavedAsteroids()) {it.asDomainModel()}
        //           Log.d(TAG, "SavedAsteroids: ${repository.getSavedAsteroids().value?.size}")
            repository.getSavedAsteroids()

        }
    }

}
