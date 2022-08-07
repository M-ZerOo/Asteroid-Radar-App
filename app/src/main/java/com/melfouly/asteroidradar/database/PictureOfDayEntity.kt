package com.melfouly.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.melfouly.asteroidradar.network.PictureOfDay

@Entity(tableName = "picture_of_day_table")
data class PictureOfDayEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "media_type") val mediaType: String,
    val title: String,
    val url: String
)

fun PictureOfDayEntity.asDomainModel(): PictureOfDay {
    return PictureOfDay(
        mediaType = mediaType,
        title = title,
        url = url
    )
}

fun PictureOfDay.asDatabaseModel(): PictureOfDayEntity {
    return PictureOfDayEntity(
        id = 1,
        mediaType = mediaType,
        title = title,
        url = url
    )
}