package com.rideconnect.data.remote.dto.request.trip

data class CreateTripRequest(
    val pickupAddress: String,
    val pickupLatitude: Double,
    val pickupLongitude: Double,
    val dropoffAddress: String,
    val dropoffLatitude: Double,
    val dropoffLongitude: Double,
    val vehicleType: String? = null,
    val scheduledTime: String? = null,
    val paymentMethodId: String? = null,
    val note: String? = null
)
 {
    data class Location(
        val latitude: Double,
        val longitude: Double,
        val address: String? = null
    )
}
