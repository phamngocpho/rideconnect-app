package com.rideconnect.data.remote.dto.request.driver

data class RegisterDriverRequest(
    val vehicleType: String,
    val vehicleModel: String,
    val vehicleColor: String,
    val licensePlate: String,
    val driverLicenseNumber: String
)
