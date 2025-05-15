<<<<<<< HEAD

=======
>>>>>>> 768b3842f0e38e4cb6f6c421e602c90773175322
package com.rideconnect.domain.repository

import com.rideconnect.data.remote.dto.request.driver.UpdateDriverStatusRequest
import retrofit2.Response

interface DriverRepository {
    suspend fun updateDriverStatus(request: UpdateDriverStatusRequest): Response<Unit>
}
<<<<<<< HEAD
=======

>>>>>>> 768b3842f0e38e4cb6f6c421e602c90773175322
