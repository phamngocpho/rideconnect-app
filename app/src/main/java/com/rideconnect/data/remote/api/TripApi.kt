package com.rideconnect.data.remote.api

import com.rideconnect.data.remote.dto.request.trip.CreateTripRequest
import com.rideconnect.data.remote.dto.request.trip.UpdateTripStatusRequest
import com.rideconnect.data.remote.dto.response.trip.TripDetailsResponse
import com.rideconnect.data.remote.dto.response.trip.TripHistoryResponse
import com.rideconnect.util.constants.ApiConstants
import retrofit2.Response
import retrofit2.http.*

interface TripApi {

    @POST(ApiConstants.TRIP_CREATE_ENDPOINT)
    suspend fun createTrip(@Body createTripRequest: CreateTripRequest): Response<TripDetailsResponse>

    @GET(ApiConstants.TRIP_HISTORY_ENDPOINT)
    suspend fun getTripHistory(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<TripHistoryResponse>

    @GET(ApiConstants.TRIP_DETAILS_ENDPOINT)
    suspend fun getTripDetails(@Path("tripId") tripId: String): Response<TripDetailsResponse>

    @PUT(ApiConstants.TRIP_STATUS_ENDPOINT)
    suspend fun updateTripStatus(
        @Path("tripId") tripId: String,
        @Body updateTripStatusRequest: UpdateTripStatusRequest
    ): Response<TripDetailsResponse>

    @DELETE(ApiConstants.TRIP_CANCEL_ENDPOINT)
    suspend fun cancelTrip(@Path("tripId") tripId: String): Response<Unit>
}
