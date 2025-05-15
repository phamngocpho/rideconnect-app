package com.rideconnect.data.remote.dto.request.trip

data class UpdateTripStatusRequest(
    val status: String,
    val cancellationReason: String ?= null
)
