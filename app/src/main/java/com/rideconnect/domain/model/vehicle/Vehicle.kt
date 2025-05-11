package com.rideconnect.domain.model.vehicle

enum class VehicleType {
    car,
    car_premium,
    bike,
    bike_plus,
    taxi,
    van
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
