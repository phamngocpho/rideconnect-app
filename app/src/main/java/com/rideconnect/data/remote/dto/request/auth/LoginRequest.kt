package com.rideconnect.data.remote.dto.request.auth

data class LoginRequest(
    val phoneNumber: String,
    val password: String
)
