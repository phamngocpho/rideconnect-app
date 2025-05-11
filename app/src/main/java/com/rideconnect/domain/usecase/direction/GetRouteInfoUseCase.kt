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

    operator fun invoke(origin: Location, destination: Location): Flow<RouteInfo?> = flow {
        val response = goongApi.getDirections(
            origin = "${origin.latitude},${origin.longitude}",
            destination = "${destination.latitude},${destination.longitude}",
            vehicle = "car"
        )

        if (response.isSuccessful) {
            val route = response.body()?.routes?.firstOrNull()
            if (route != null) {
                val leg = route.legs.firstOrNull()
                emit(RouteInfo(
                    distance = leg?.distance?.value ?: 0,
                    duration = leg?.duration?.value ?: 0
                ))
            } else {
                Log.w("GetRouteInfoUseCase", "No route found")
                emit(null)
            }
        } else {
            Log.e("GetRouteInfoUseCase", "API Error: ${response.code()}")
            emit(null)  // Emit null instead of throwing
        }
    }.catch { e ->
        Log.e("GetRouteInfoUseCase", "Exception in flow: ${e.message}", e)
        emit(null) // Emit null to prevent AbortFlowException
    }

}