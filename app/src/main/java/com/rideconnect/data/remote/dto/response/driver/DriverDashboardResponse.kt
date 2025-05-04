package com.rideconnect.data.remote.dto.response.driver

import java.math.BigDecimal

data class DriverDashboardResponse(
    val currentStatus: String, // "ONLINE", "OFFLINE", "BUSY"
    val todayEarnings: BigDecimal,
    val weeklyEarnings: BigDecimal,
    val completedTripsToday: Int,
    val rating: Double,
    val activeTrip: TripSummary?
) {
    data class TripSummary(
        val tripId: String,
        val customerName: String,
        val destination: String,
        val status: String
    )
}
