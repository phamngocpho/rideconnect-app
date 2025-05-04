package com.rideconnect.data.remote.dto.request.driver

data class UpdateDriverStatusRequest(
    val status: String // "ONLINE", "OFFLINE", "BUSY"
)
