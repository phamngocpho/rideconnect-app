package com.rideconnect.data.remote.api

import com.rideconnect.data.remote.dto.request.location.LocationUpdateRequest
import com.rideconnect.data.remote.dto.response.location.NearbyDriversResponse
import com.rideconnect.util.constants.ApiConstants
import retrofit2.Response
import retrofit2.http.*

interface LocationApi {

    @POST(ApiConstants.LOCATION_UPDATE_ENDPOINT)
    suspend fun updateLocation(@Body locationUpdateRequest: LocationUpdateRequest): Response<Unit>

    @GET(ApiConstants.NEARBY_DRIVERS_ENDPOINT)
    suspend fun getNearbyDrivers(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radiusInKm: Double = 5.0
    ): Response<NearbyDriversResponse>
}
