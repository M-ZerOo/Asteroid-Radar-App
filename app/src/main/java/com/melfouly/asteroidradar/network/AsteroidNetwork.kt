package com.melfouly.asteroidradar.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AsteroidNetwork(
    val id: Long, val codeName: String, val closeApproachDate: String,
    val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)
