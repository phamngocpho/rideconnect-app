package com.rideconnect.data.remote.dto.request.location

data class LocationUpdateRequest(
    val latitude: Double,
    val longitude: Double,
    val heading: Float? = null,
    val speed: Float? = null,
    val bearing: Float = 0f,
    val accuracy: Float = 0f,
    val timestamp: Long
)
