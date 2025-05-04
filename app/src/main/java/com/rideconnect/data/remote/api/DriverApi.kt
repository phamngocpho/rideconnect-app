package com.rideconnect.data.remote.api

import com.rideconnect.data.remote.dto.request.driver.RegisterDriverRequest
import com.rideconnect.data.remote.dto.request.driver.UpdateDriverStatusRequest
import com.rideconnect.data.remote.dto.response.driver.DriverDashboardResponse
import com.rideconnect.data.remote.dto.response.driver.DriverProfileResponse
import com.rideconnect.util.constants.ApiConstants
import retrofit2.Response
import retrofit2.http.*

interface DriverApi {

    @POST(ApiConstants.DRIVER_REGISTER_ENDPOINT)
    suspend fun registerAsDriver(@Body registerDriverRequest: RegisterDriverRequest): Response<Unit>

    @GET(ApiConstants.DRIVER_DASHBOARD_ENDPOINT)
    suspend fun getDashboard(): Response<DriverDashboardResponse>

    @GET(ApiConstants.DRIVER_PROFILE_ENDPOINT)
    suspend fun getProfile(): Response<DriverProfileResponse>

    @PUT(ApiConstants.DRIVER_STATUS_ENDPOINT)
    suspend fun updateStatus(@Body updateDriverStatusRequest: UpdateDriverStatusRequest): Response<Unit>
}
