package com.rideconnect.data.remote.dto.response.customer

import java.time.ZonedDateTime

data class CustomerDashboardResponse(
    val upcomingTrips: List<TripSummary>,
    val recentTrips: List<TripSummary>,
    val savedAddresses: List<SavedAddress>
) {
    data class TripSummary(
        val tripId: String,
        val destination: String,
        val scheduledTime: ZonedDateTime?,
        val status: String
    )

    data class SavedAddress(
        val id: String,
        val name: String,
        val address: String,
        val type: String // "HOME", "WORK", "OTHER"
    )
}
