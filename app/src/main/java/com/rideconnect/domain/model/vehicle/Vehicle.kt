package com.rideconnect.domain.model.vehicle

enum class VehicleType {
    CAR,
    CAR_PREMIUM,
    BIKE,
    BIKE_PLUS,
    TAXI,
    VAN
}

data class Vehicle(
    val id: String,
    val type: VehicleType,
    val name: String,
    val description: String,
    val price: Double,
    val estimatedPickupTime: Int,
    val capacity: Int
) {
    val formattedPrice: String
        get() = "${price.toLong()}đ"
}
