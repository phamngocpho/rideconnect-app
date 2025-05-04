package com.rideconnect.data.remote.dto.request.customer

data class SaveAddressRequest(
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val type: String // "HOME", "WORK", "OTHER"
)
