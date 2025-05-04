package com.rideconnect.domain.model.driver

import com.rideconnect.domain.model.user.User
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Locale

data class Driver(
    val id: String,
    val user: User,
    val licenseNumber: String,
    val licenseExpiryDate: ZonedDateTime,
    val vehicleModel: String,
    val vehicleColor: String,
    val vehiclePlateNumber: String,
    val rating: Float,
    val isAvailable: Boolean,
    val isVerified: Boolean,
    val currentLocation: DriverLocation? = null,
    val earnings: BigDecimal = BigDecimal.ZERO,
    val totalTrips: Int = 0
) {
    fun getVehicleInfo(): String {
        return "$vehicleColor $vehicleModel - $vehiclePlateNumber"
    }

    fun getFormattedRating(locale: Locale = Locale.US): String {
        return String.format(locale, "%.1f", rating)
    }

    fun getRatingStars(): Float {
        return rating
    }

    fun isLicenseValid(): Boolean {
        return licenseExpiryDate.isAfter(ZonedDateTime.now())
    }

    fun getLicenseExpiryStatus(): String {
        val now = ZonedDateTime.now()
        val months = ChronoUnit.MONTHS.between(now, licenseExpiryDate)

        return when {
            licenseExpiryDate.isBefore(now) -> "Expired"
            months < 1 -> "Expires soon"
            months < 3 -> "Expires in $months months"
            else -> "Valid"
        }
    }

    fun getStatusText(): String {
        return if (isAvailable) "Available" else "Unavailable"
    }

    fun getFormattedEarnings(locale: Locale = Locale.US): String {
        return NumberFormat.getCurrencyInstance(locale).format(earnings)
    }
}

// UI State cho Driver Profile Screen
data class DriverProfileUiState(
    val driver: Driver? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditMode: Boolean = false
)

// UI State cho Driver Status
data class DriverStatusUiState(
    val isAvailable: Boolean = false,
    val isToggling: Boolean = false,
    val currentLocation: DriverLocation? = null,
    val error: String? = null
)
