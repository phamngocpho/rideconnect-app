package com.rideconnect.domain.repository

import com.rideconnect.data.remote.dto.request.rating.CreateRatingRequest
import com.rideconnect.data.remote.dto.response.rating.RatingResponse
import com.rideconnect.util.Resource

interface RatingRepository {
    suspend fun createRating(tripId: String, createRatingRequest: CreateRatingRequest): Resource<RatingResponse>
    suspend fun getUserRatings(userId: String, page: Int = 0, size: Int = 10): Resource<List<RatingResponse>>
    suspend fun getTripRating(tripId: String): Resource<RatingResponse>
}
