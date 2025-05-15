package com.rideconnect.data.remote.dto.request.driver

data class UpdateDriverStatusRequest(
    val isAvailable: String // "ONLINE", "OFFLINE", "BUSY"
)
