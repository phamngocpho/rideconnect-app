package com.rideconnect.data.remote.dto.request.location

data class LocationUpdateRequest(
    val latitude: Double,
    val longitude: Double,
    val heading: Float? = null,
    val speed: Float? = null
)
