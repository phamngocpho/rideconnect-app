package com.rideconnect.domain.usecase.rating

import com.rideconnect.data.remote.dto.response.rating.RatingResponse
import com.rideconnect.domain.repository.RatingRepository
import com.rideconnect.util.Resource
import javax.inject.Inject

class GetTripRatingUseCase @Inject constructor(
    private val ratingRepository: RatingRepository
) {
    suspend operator fun invoke(tripId: String): Resource<RatingResponse> {
        return ratingRepository.getTripRating(tripId)
    }
}
