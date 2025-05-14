package com.rideconnect.domain.usecase.trip

import com.rideconnect.data.remote.dto.request.trip.CreateTripRequest
import com.rideconnect.data.remote.dto.response.trip.TripDetailsResponse
import com.rideconnect.data.repository.TripRepositoryImpl
import com.rideconnect.domain.model.trip.Trip
import com.rideconnect.domain.repository.TripRepository
import com.rideconnect.util.Resource
import com.rideconnect.util.Result
import javax.inject.Inject

class CreateTripUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(request: CreateTripRequest): Resource<Trip> {
        return tripRepository.createTrip(request)
    }
}

