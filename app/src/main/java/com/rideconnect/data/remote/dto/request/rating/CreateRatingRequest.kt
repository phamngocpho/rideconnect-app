package com.rideconnect.data.remote.dto.request.rating

data class CreateRatingRequest(
    val tripId: String,
    val rating: Int, // 1-5
    val comment: String? = null
)
