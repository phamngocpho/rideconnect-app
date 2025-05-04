package com.rideconnect.data.remote.dto.request.auth

data class RegisterRequest(
    val phoneNumber: String,
    val email: String?,
    val password: String,
    val fullName: String
)
