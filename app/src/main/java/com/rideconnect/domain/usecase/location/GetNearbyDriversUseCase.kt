package com.rideconnect.domain.usecase.location

import com.rideconnect.data.remote.dto.request.location.NearbyDriversRequest
import com.rideconnect.data.remote.dto.response.location.NearbyDriversResponse
import com.rideconnect.domain.repository.LocationRepository
import retrofit2.Response
import javax.inject.Inject

class GetNearbyDriversUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(request: NearbyDriversRequest): Response<NearbyDriversResponse> {
        return locationRepository.getNearbyDrivers(request)
    }
}
