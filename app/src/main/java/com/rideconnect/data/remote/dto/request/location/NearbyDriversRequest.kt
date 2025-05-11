package com.rideconnect.data.remote.dto.request.location

import com.google.gson.annotations.SerializedName
import java.io.Serializable // Important for passing between screens if needed

data class NearbyDriversRequest(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("radiusInKm")  //  Match Java field name
    val radiusInKm: Double?, // Or Double if it can be null
    @SerializedName("vehicleType")
    val vehicleType: String
) : Serializable  // Implement Serializable