package com.rideconnect.data.remote.dto.response.driver

import java.math.BigDecimal
import java.time.ZonedDateTime

data class DriverProfileResponse(
    val userId: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val profilePictureUrl: String?,
    val rating: Double,
    val memberSince: ZonedDateTime,
    val completedTrips: Int,
    val vehicleDetails: VehicleDetails,
    val totalEarnings: BigDecimal
) {
    data class VehicleDetails(
        val type: String,
        val model: String,
        val color: String,
        val licensePlate: String
    )
}
