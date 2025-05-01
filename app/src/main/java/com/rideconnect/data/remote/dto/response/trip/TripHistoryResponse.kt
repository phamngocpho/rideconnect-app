package com.rideconnect.data.remote.dto.response.trip

import java.math.BigDecimal
import java.time.ZonedDateTime

data class TripHistoryResponse(
    val trips: List<TripSummary>
) {
    data class TripSummary(
        val tripId: String,
        val date: ZonedDateTime,
        val pickupAddress: String,
        val dropOffAddress: String,
        val status: String,
        val fare: BigDecimal?,
        val distance: Double?, // in kilometers
        val duration: Int?, // in minutes
        val driverName: String?,
        val vehicleType: String?,
        val rating: Int? // 1-5
    )
}
