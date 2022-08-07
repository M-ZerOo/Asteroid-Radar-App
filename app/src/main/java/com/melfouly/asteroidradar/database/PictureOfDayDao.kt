package com.melfouly.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PictureOfDayDao {
    @Query("select * from picture_of_day_table limit 1")
    fun getPictureOfDay(): LiveData<PictureOfDayEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(pictureOfDayEntity: PictureOfDayEntity)
}