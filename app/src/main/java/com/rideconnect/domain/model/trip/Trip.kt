package com.rideconnect.domain.model.trip

import java.math.BigDecimal
import java.time.ZonedDateTime

data class Trip(
    val id: String,
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
    val dropOffLatitude: Double,
    val dropOffLongitude: Double,
    val dropOffAddress: String,
    val status: TripStatus,
    val estimatedDistance: Double,
    val estimatedDuration: Int,
    val estimatedFare: BigDecimal,
    val actualFare: BigDecimal?,
    val requestedAt: String,
    val startedAt: String?,
    val completedAt: String?,
    val cancelledAt: String?,
    val cancellationReason: String?,
    val driverLocation: DriverLocation?
)

data class DriverLocation(
    val latitude: Double,
    val longitude: Double,
    val heading: Double,
    val lastUpdated: String?
)