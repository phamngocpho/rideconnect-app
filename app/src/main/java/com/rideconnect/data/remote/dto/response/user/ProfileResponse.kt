package com.rideconnect.data.remote.dto.response.user

import java.time.ZonedDateTime

data class ProfileResponse(
    val userId: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val profilePictureUrl: String?,
    val userType: String, // "CUSTOMER", "DRIVER", "ADMIN"
    val memberSince: ZonedDateTime,
    val isVerified: Boolean
)
