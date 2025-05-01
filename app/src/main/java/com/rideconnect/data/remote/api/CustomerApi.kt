package com.rideconnect.data.remote.api

import com.rideconnect.data.remote.dto.request.customer.SaveAddressRequest
import com.rideconnect.data.remote.dto.response.customer.CustomerDashboardResponse
import com.rideconnect.data.remote.dto.response.customer.CustomerProfileResponse
import com.rideconnect.util.constants.ApiConstants
import retrofit2.Response
import retrofit2.http.*

interface CustomerApi {

    @GET(ApiConstants.CUSTOMER_DASHBOARD_ENDPOINT)
    suspend fun getDashboard(): Response<CustomerDashboardResponse>

    @GET(ApiConstants.CUSTOMER_PROFILE_ENDPOINT)
    suspend fun getProfile(): Response<CustomerProfileResponse>

    @POST(ApiConstants.CUSTOMER_ADDRESS_ENDPOINT)
    suspend fun saveAddress(@Body saveAddressRequest: SaveAddressRequest): Response<Unit>

    @DELETE(ApiConstants.CUSTOMER_ADDRESS_ENDPOINT + "/{addressId}")
    suspend fun deleteAddress(@Path("addressId") addressId: String): Response<Unit>
}
