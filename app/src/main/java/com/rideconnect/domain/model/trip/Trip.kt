package com.rideconnect.domain.model.trip

import android.media.Rating
import com.rideconnect.domain.model.customer.Customer
import com.rideconnect.domain.model.driver.Driver
import com.rideconnect.domain.model.payment.Payment
import org.maplibre.android.geometry.LatLng
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class Trip(
    val id: String,
    val customerId: String,
    val driverId: String?,
    val pickupLatitude: Double,
    val pickupLongitude: Double,
    val pickupAddress: String,
    val dropOffLatitude: Double,
    val dropOffLongitude: Double,
    val dropOffAddress: String,
    val routeGeometry: String?, // GeoJSON or encoded polyline
    val status: TripStatus,
    val fare: BigDecimal,
    val distance: Double, // in kilometers
    val duration: Int, // in minutes
    val requestedAt: ZonedDateTime,
    val acceptedAt: ZonedDateTime?,
    val startedAt: ZonedDateTime?,
    val completedAt: ZonedDateTime?,
    val cancelledAt: ZonedDateTime?,
    val paymentId: String?,
    val customer: Customer? = null,
    val driver: Driver? = null,
    val payment: Payment? = null,
    val rating: Rating? = null
) {
    fun getPickupLatLng(): LatLng {
        return LatLng(pickupLatitude, pickupLongitude)
    }

    fun getDropOffLatLng(): LatLng {
        return LatLng(dropOffLatitude, dropOffLongitude)
    }

    fun getFormattedFare(): String {
        return NumberFormat.getCurrencyInstance().format(fare)
    }

    fun getFormattedDistance(): String {
        return String.format("%.1f km", distance)
    }

    fun getFormattedDuration(): String {
        return when {
            duration < 60 -> "$duration mins"
            else -> "${duration / 60} hr ${duration % 60} mins"
        }
    }

    fun getStatusText(): String {
        return when (status) {
            TripStatus.PENDING -> "Pending"
            TripStatus.ACCEPTED -> "Accepted"
            TripStatus.ARRIVED -> "Driver Arrived"
            TripStatus.IN_PROGRESS -> "In Progress"
            TripStatus.COMPLETED -> "Completed"
            TripStatus.CANCELLED -> "Cancelled"
        }
    }

    fun getPickupShortAddress(): String {
        return pickupAddress.split(",").take(2).joinToString(",")
    }

    fun getDropOffShortAddress(): String {
        return dropOffAddress.split(",").take(2).joinToString(",")
    }

    fun canCancel(): Boolean {
        return status == TripStatus.PENDING || status == TripStatus.ACCEPTED
    }

    fun canRate(): Boolean {
        return status == TripStatus.COMPLETED && rating == null
    }

    fun isActive(): Boolean {
        return status == TripStatus.PENDING ||
                status == TripStatus.ACCEPTED ||
                status == TripStatus.ARRIVED ||
                status == TripStatus.IN_PROGRESS
    }

    fun getTripDate(): String {
        return requestedAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }

    fun getTripTime(): String {
        return requestedAt.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun getEta(): String {
        // Calculate ETA based on current status and time
        return when (status) {
            TripStatus.PENDING -> "Searching driver..."
            TripStatus.ACCEPTED -> "Driver arriving in ${calculateDriverArrivalTime()} mins"
            TripStatus.ARRIVED -> "Driver has arrived"
            TripStatus.IN_PROGRESS -> "Arriving in ${calculateRemainingTime()} mins"
            else -> ""
        }
    }

    private fun calculateDriverArrivalTime(): Int {
        // This would use actual distance calculation in a real app
        return 5 // Placeholder
    }

    private fun calculateRemainingTime(): Int {
        // This would use actual progress calculation in a real app
        return 10 // Placeholder
    }
}

// UI State cho Trip List Screen
data class TripListUiState(
    val trips: List<TripUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filterType: TripFilterType = TripFilterType.ALL
)

data class TripUiModel(
    val trip: Trip,
    val isExpanded: Boolean = false
)

enum class TripFilterType {
    ALL, ACTIVE, COMPLETED, CANCELLED
}

// UI State cho Trip Detail Screen
data class TripDetailUiState(
    val trip: Trip? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showCancelDialog: Boolean = false,
    val showRatingDialog: Boolean = false
)

// UI State cho Trip Request Screen
data class TripRequestUiState(
    val pickupLocation: LocationModel? = null,
    val dropOffLocation: LocationModel? = null,
    val estimatedFare: BigDecimal? = null,
    val estimatedDistance: Double? = null,
    val estimatedDuration: Int? = null,
    val isRequestingTrip: Boolean = false,
    val error: String? = null,
    val availableDrivers: Int = 0
)

data class LocationModel(
    val address: String,
    val latLng: LatLng
)
