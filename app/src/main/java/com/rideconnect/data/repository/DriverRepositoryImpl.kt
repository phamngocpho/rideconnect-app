package com.rideconnect.data.repository

import com.rideconnect.data.remote.api.DriverApi
import com.rideconnect.data.remote.dto.request.driver.UpdateDriverStatusRequest
import com.rideconnect.data.remote.dto.response.driver.DriverProfileResponse
import com.rideconnect.domain.repository.DriverRepository
import retrofit2.Response
import javax.inject.Inject

class DriverRepositoryImpl @Inject constructor(
    private val driverApi: DriverApi
) : DriverRepository {

    override suspend fun updateDriverStatus(request: UpdateDriverStatusRequest): Response<Unit> {
        return driverApi.updateDriverStatus(request)
    }

}
