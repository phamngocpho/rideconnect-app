package com.rideconnect.domain.usecase.rating

import com.rideconnect.data.remote.dto.request.rating.CreateRatingRequest
import com.rideconnect.data.remote.dto.response.rating.RatingResponse
import com.rideconnect.domain.repository.RatingRepository
import com.rideconnect.util.Resource
import javax.inject.Inject

class CreateRatingUseCase @Inject constructor(
    private val ratingRepository: RatingRepository
) {
    suspend operator fun invoke(tripId: String, rating: Int, comment: String? = null): Resource<RatingResponse> {
        val request = CreateRatingRequest(
            rating = rating,
            comment = comment
        )
        return ratingRepository.createRating(tripId, request)
    }
}
