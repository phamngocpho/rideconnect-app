package com.rideconnect.data.remote.api

import com.rideconnect.data.remote.dto.request.rating.CreateRatingRequest
import com.rideconnect.data.remote.dto.response.rating.RatingResponse
import com.rideconnect.util.constants.ApiConstants
import retrofit2.Response
import retrofit2.http.*

interface RatingApi {

    @POST(ApiConstants.RATING_CREATE_ENDPOINT)
    suspend fun createRating(@Body createRatingRequest: CreateRatingRequest): Response<RatingResponse>

    @GET(ApiConstants.RATING_USER_ENDPOINT)
    suspend fun getUserRatings(
        @Path("userId") userId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<List<RatingResponse>>

    @GET(ApiConstants.RATING_TRIP_ENDPOINT)
    suspend fun getTripRating(@Path("tripId") tripId: String): Response<RatingResponse>
}
