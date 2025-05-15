package com.rideconnect.data.remote.dto.response.trip

import java.math.BigDecimal
import java.time.ZonedDateTime

data class TripDetailsResponse(
    val tripId: String,
    val customerId: String,
    val customerName: String,
    val customerPhone: String,
    val driverId: String?,
    val driverName: String?,
    val driverPhone: String?,
    val vehicleType: String?,
    val vehiclePlate: String?,
    val pickupLatitude: Double,
    val pickupLongitude: Double,
    val pickupAddress: String,
    val dropoffLatitude: Double,
    val dropoffLongitude: Double,
    val dropoffAddress: String,
    val status: String,
    val estimatedDistance: Double,
    val estimatedDuration: Int,
    val estimatedFare: BigDecimal,
    val actualFare: BigDecimal?,
    val createdAt: String,
    val startedAt: String?,
    val completedAt: String?,
    val cancelledAt: String?,
    val cancellationReason: String?,
    val driverLocation: DriverLocation?
) {
    data class DriverLocation(
        val latitude: Double,
        val longitude: Double,
        val heading: Double,
        val lastUpdated: String?
    )
}