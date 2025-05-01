package com.rideconnect.data.remote.dto.response.rating

import java.time.ZonedDateTime

data class RatingResponse(
    val ratingId: String,
    val tripId: String,
    val rating: Int, // 1-5
    val comment: String?,
    val ratedBy: String, // User ID who gave the rating
    val ratedByName: String,
    val timestamp: ZonedDateTime
)
