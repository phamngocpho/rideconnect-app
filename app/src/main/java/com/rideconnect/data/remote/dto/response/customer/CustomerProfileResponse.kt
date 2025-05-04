package com.rideconnect.data.remote.dto.response.customer

import java.time.ZonedDateTime

data class CustomerProfileResponse(
    val userId: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val profilePictureUrl: String?,
    val rating: Double,
    val memberSince: ZonedDateTime,
    val completedTrips: Int,
    val savedPaymentMethods: Int
)
