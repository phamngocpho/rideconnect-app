package com.rideconnect.data.repository

import com.rideconnect.data.remote.api.DriverApi
import com.rideconnect.data.remote.dto.request.driver.UpdateDriverStatusRequest
<<<<<<< HEAD
=======
import com.rideconnect.data.remote.dto.response.driver.DriverProfileResponse
>>>>>>> 768b3842f0e38e4cb6f6c421e602c90773175322
import com.rideconnect.domain.repository.DriverRepository
import retrofit2.Response
import javax.inject.Inject

class DriverRepositoryImpl @Inject constructor(
    private val driverApi: DriverApi
) : DriverRepository {

    override suspend fun updateDriverStatus(request: UpdateDriverStatusRequest): Response<Unit> {
        return driverApi.updateDriverStatus(request)
    }

<<<<<<< HEAD
}
=======
}
>>>>>>> 768b3842f0e38e4cb6f6c421e602c90773175322
