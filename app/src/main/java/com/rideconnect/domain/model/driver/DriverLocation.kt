package com.rideconnect.domain.model.driver

import org.maplibre.android.geometry.LatLng
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

data class DriverLocation(
    val id: String,
    val driverId: String,
    val latitude: Double,
    val longitude: Double,
    val heading: Double,
    val timestamp: ZonedDateTime
) {
    fun toLatLng(): LatLng {
        return LatLng(latitude, longitude)
    }

    fun getFormattedTimestamp(): String {
        val now = ZonedDateTime.now()
        val minutes = ChronoUnit.MINUTES.between(timestamp, now)

        return when {
            minutes < 1 -> "Just now"
            minutes < 60 -> "$minutes minutes ago"
            else -> "Over an hour ago"
        }
    }

    fun isRecent(): Boolean {
        val now = ZonedDateTime.now()
        val minutes = ChronoUnit.MINUTES.between(timestamp, now)
        return minutes < 5
    }
}

// UI State cho Map Screen
data class MapUiState(
    val driverLocations: List<DriverLocationUiModel> = emptyList(),
    val selectedDriverId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val userLocation: LatLng? = null
)

data class DriverLocationUiModel(
    val driverLocation: DriverLocation,
    val driver: Driver? = null,
    val isSelected: Boolean = false
)
