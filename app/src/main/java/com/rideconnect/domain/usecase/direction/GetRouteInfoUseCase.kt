package com.rideconnect.domain.usecase.direction

import android.util.Log
import com.rideconnect.data.remote.api.GoongMapApi
import com.rideconnect.domain.model.location.Location
import com.rideconnect.domain.model.direction.RouteInfo
import com.rideconnect.domain.repository.LocationRepository // Import LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class GetRouteInfoUseCase @Inject constructor(
    private val goongApi: GoongMapApi
) {

    operator fun invoke(origin: Location, destination: Location): Flow<RouteInfo?> = flow {  // Changed to Flow<RouteInfo?>
        try {
            val response = goongApi.getDirections(
                origin = "${origin.latitude},${origin.longitude}",
                destination = "${destination.latitude},${destination.longitude}",
                vehicle = "car"
            )

            if (response.isSuccessful) {
                val route = response.body()?.routes?.firstOrNull()
                if (route != null) {
                    val distance = route.legs.firstOrNull()?.distance?.value ?: 0
                    val duration = route.legs.firstOrNull()?.duration?.value ?: 0
                    emit(RouteInfo(distance = distance, duration = duration))
                } else {
                    Log.w("GetRouteInfoUseCase", "No route found in API response")
                    emit(null) // Emit null to indicate no route, handled by catch
                }
            } else {
                Log.e("GetRouteInfoUseCase", "API error: ${response.code()} - ${response.message()}")
                throw IOException("Error fetching route info: ${response.code()} - ${response.message()}") // Throw exception
            }
        } catch (e: IOException) {
            Log.e("GetRouteInfoUseCase", "Network or parsing error: ${e.message}", e)
            throw e  // Re-throw the exception to be caught in GetAvailableVehiclesUseCase
        } catch (e: Exception) {
            Log.e("GetRouteInfoUseCase", "Unexpected error: ${e.message}", e)
            throw e
        }
    }
}