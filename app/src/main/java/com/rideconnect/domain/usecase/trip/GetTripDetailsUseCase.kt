package com.rideconnect.domain.usecase.trip

import com.rideconnect.domain.model.trip.Trip
import com.rideconnect.domain.repository.TripRepository
import com.rideconnect.util.Resource
import javax.inject.Inject

class GetTripDetailsUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {
    suspend operator fun invoke(tripId: String): Resource<Trip> {
        return tripRepository.getTripDetails(tripId)
    }
}
