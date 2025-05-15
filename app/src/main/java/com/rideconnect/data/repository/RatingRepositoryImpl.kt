package com.rideconnect.data.repository

import com.rideconnect.data.remote.api.RatingApi
import com.rideconnect.data.remote.dto.request.rating.CreateRatingRequest
import com.rideconnect.data.remote.dto.response.rating.RatingResponse
import com.rideconnect.domain.repository.RatingRepository
import com.rideconnect.util.Resource
import javax.inject.Inject

class RatingRepositoryImpl @Inject constructor(
    private val ratingApi: RatingApi
) : RatingRepository {

    override suspend fun createRating(tripId: String, createRatingRequest: CreateRatingRequest): Resource<RatingResponse> {
        return try {
            val response = ratingApi.createRating(tripId, createRatingRequest)
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Response body is null")
            } else {
                Resource.Error("Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun getUserRatings(userId: String, page: Int, size: Int): Resource<List<RatingResponse>> {
        return try {
            val response = ratingApi.getUserRatings(userId, page, size)
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Response body is null")
            } else {
                Resource.Error("Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun getTripRating(tripId: String): Resource<RatingResponse> {
        return try {
            val response = ratingApi.getTripRating(tripId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Response body is null")
            } else {
                Resource.Error("Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
}
