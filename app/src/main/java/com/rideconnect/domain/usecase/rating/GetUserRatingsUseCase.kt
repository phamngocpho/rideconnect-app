package com.rideconnect.domain.usecase.rating

import com.rideconnect.data.remote.dto.response.rating.RatingResponse
import com.rideconnect.domain.repository.RatingRepository
import com.rideconnect.util.Resource
import javax.inject.Inject

class GetUserRatingsUseCase @Inject constructor(
    private val ratingRepository: RatingRepository
) {
    suspend operator fun invoke(userId: String, page: Int = 0, size: Int = 10): Resource<List<RatingResponse>> {
        return ratingRepository.getUserRatings(userId, page, size)
    }
}
