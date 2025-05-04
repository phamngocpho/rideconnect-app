package com.rideconnect.data.remote.dto.response.auth

data class LoginResponse(
    val userId: String,
    val token: String,
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val avatarUrl: String? = null,
    val userType: String? = null,
    val expiresAt: String? = null
)

