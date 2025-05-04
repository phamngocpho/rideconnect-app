package com.rideconnect.data.remote.dto.request.trip

data class CreateTripRequest(
    val pickupLocation: Location,
    val dropOffLocation: Location,
    val vehicleType: String? = null,
    val scheduledTime: String? = null,
    val paymentMethodId: String? = null,
    val note: String? = null
) {
    data class Location(
        val latitude: Double,
        val longitude: Double,
        val address: String
    )
}
