package com.melfouly.asteroidradar

import android.app.Application
import com.melfouly.asteroidradar.database.AsteroidDatabase
import com.melfouly.asteroidradar.database.getDatabase

class BaseApplication : Application() {
    val database: AsteroidDatabase by lazy { getDatabase(this) }
}