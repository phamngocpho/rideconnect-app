package com.rideconnect.domain.usecase.trip

import com.rideconnect.domain.repository.TripRepository
import javax.inject.Inject

class CancelTripUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {
    operator fun invoke() {
        tripRepository.clearCurrentTrip()
    }
}
