package com.melfouly.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.melfouly.asteroidradar.model.Asteroid

@Dao
interface AsteroidDao {
    @Query("select * from asteroid_table order by closeApproachDate asc")
    fun getAll(): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroid_table where closeApproachDate >= :startDate and closeApproachDate <= :endDate order by closeApproachDate asc")
    fun getAsteroidsOfToday(startDate: String, endDate: String): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroid_table where id = :id")
    fun get(id: Long): LiveData<AsteroidEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroidEntity: AsteroidEntity)

    @Query("delete from asteroid_table where closeApproachDate < :day")
    suspend fun deletePreviousDay(day: String)
}