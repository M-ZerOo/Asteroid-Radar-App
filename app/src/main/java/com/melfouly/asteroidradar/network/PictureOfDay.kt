package com.melfouly.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

@JsonClass(generateAdapter = true)
data class PictureOfDay(
    @Json(name = "media_type") val mediaType: String,
    val title: String,
    val url: String
)
