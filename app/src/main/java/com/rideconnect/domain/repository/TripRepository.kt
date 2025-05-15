package com.rideconnect.domain.repository

import com.rideconnect.data.remote.dto.request.trip.CreateTripRequest
import com.rideconnect.data.remote.dto.request.trip.UpdateTripStatusRequest
import com.rideconnect.domain.model.trip.Trip
import com.rideconnect.util.Resource
import kotlinx.coroutines.flow.StateFlow

interface TripRepository {
    val currentTrip: StateFlow<Trip?>
    suspend fun createTrip(createTripRequest: CreateTripRequest): Resource<Trip>
    suspend fun getTripDetails(tripId: String): Resource<Trip>
    suspend fun updateTripStatus(tripId: String, updateTripStatusRequest: UpdateTripStatusRequest): Resource<Trip>
    fun clearCurrentTrip()
}