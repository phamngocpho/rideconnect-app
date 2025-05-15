
package com.rideconnect.domain.repository

import com.rideconnect.data.remote.dto.request.driver.UpdateDriverStatusRequest
import retrofit2.Response

interface DriverRepository {
    suspend fun updateDriverStatus(request: UpdateDriverStatusRequest): Response<Unit>
}
