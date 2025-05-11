package com.rideconnect.domain.usecase.driver

import android.util.Log
import com.mapbox.geojson.Point
import com.rideconnect.domain.repository.LocationRepository
import com.rideconnect.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    private val TAG = "UpdateLocationUseCase"

    operator fun invoke(point: Point): Flow<Resource<Boolean>> = flow {
        Log.d(TAG, "invoke: Bắt đầu cập nhật vị trí - lat=${point.latitude()}, lng=${point.longitude()}")
        emit(Resource.Loading())

        try {
            Log.d(TAG, "invoke: Gọi repository để cập nhật vị trí")
            val result = locationRepository.updateDriverLocation(point)

            if (result.isSuccessful) {
                Log.d(TAG, "invoke: Cập nhật vị trí thành công - HTTP ${result.code()}")
                emit(Resource.Success(true))
            } else {
                val errorMessage = result.message() ?: "Failed to update location"
                Log.e(TAG, "invoke: Cập nhật vị trí thất bại - HTTP ${result.code()}, message: $errorMessage")
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            val errorMessage = e.localizedMessage ?: "An unexpected error occurred"
            Log.e(TAG, "invoke: Exception khi cập nhật vị trí - $errorMessage", e)
            emit(Resource.Error(errorMessage))
        }
    }
}
