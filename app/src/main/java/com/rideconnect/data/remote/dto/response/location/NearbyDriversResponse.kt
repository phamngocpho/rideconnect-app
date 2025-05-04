package com.rideconnect.data.remote.dto.response.location

data class NearbyDriversResponse(
    val drivers: List<DriverInfo>,
    val count: Int
)

data class DriverInfo(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val distance: Double,
    val rating: Float,
    val vehicleInfo: VehicleInfo?
)

data class VehicleInfo(
    val model: String,
    val color: String,
    val plateNumber: String,
    val vehicleType: String
)
