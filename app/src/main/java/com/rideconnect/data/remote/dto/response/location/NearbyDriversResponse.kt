package com.rideconnect.data.remote.dto.response.location

import com.google.gson.annotations.SerializedName

data class NearbyDriversResponse(
    val drivers: List<DriverInfo>,
    val count: Int
)

data class DriverInfo(
    @SerializedName("driverId") val id: String,
    val latitude: Double,
    val longitude: Double,
    val distance: Double,
    @SerializedName("vehiclePlate") val plateNumber: String,
    @SerializedName("vehicleType") val vehicleType: String,
    @SerializedName("heading") val heading: Float? = null,
    @SerializedName("estimatedArrivalTime") val estimatedArrivalTime: Int? = null,
    val name: String? = null, // Nullable, as not in response
    val rating: Float? = null, // Nullable, as not in response
    val vehicleInfo: VehicleInfo? = null // Nullable, as not in response
)

data class VehicleInfo(
    val model: String? = null,
    val color: String? = null,
    val plateNumber: String? = null,
    val vehicleType: String? = null
)