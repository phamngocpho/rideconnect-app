package com.rideconnect.data.remote.dto.response.trip

import java.math.BigDecimal
import java.time.ZonedDateTime

data class TripDetailsResponse(
    val tripId: String,
    val customerId: String,
    val customerName: String,
    val customerPhone: String?,
    val driverId: String?,
    val driverName: String?,
    val driverPhone: String?,
    val vehicleDetails: VehicleDetails?,
    val pickupLocation: Location,
    val dropOffLocation: Location,
    val status: String, // "PENDING", "ACCEPTED", "ARRIVED", "STARTED", "COMPLETED", "CANCELLED"
    val createdAt: ZonedDateTime,
    val scheduledTime: ZonedDateTime?,
    val startTime: ZonedDateTime?,
    val endTime: ZonedDateTime?,
    val fare: BigDecimal?,
    val distance: Double?, // in kilometers
    val duration: Int?, // in minutes
    val paymentStatus: String?, // "PENDING", "COMPLETED", "FAILED"
    val paymentMethod: String?,
    val rating: Int?, // 1-5
    val note: String?
) {
    data class VehicleDetails(
        val type: String,
        val model: String,
        val color: String,
        val licensePlate: String
    )

    data class Location(
        val latitude: Double,
        val longitude: Double,
        val address: String
    )
}
