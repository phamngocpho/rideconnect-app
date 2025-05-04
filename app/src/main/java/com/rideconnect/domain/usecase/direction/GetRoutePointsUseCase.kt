package com.rideconnect.domain.usecase.direction

import com.mapbox.geojson.Point
import com.rideconnect.domain.model.location.Location
import com.rideconnect.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRoutePointsUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(source: Location, destination: Location): Flow<List<Point>> = flow {
        try {
            val points = locationRepository.getRoutePoints(
                sourceLatitude = source.latitude,
                sourceLongitude = source.longitude,
                destLatitude = destination.latitude,
                destLongitude = destination.longitude
            )
            emit(points)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }
}
