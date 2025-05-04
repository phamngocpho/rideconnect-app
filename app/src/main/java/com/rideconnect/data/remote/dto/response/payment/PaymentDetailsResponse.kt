package com.rideconnect.data.remote.dto.response.payment

import java.math.BigDecimal
import java.time.ZonedDateTime

data class PaymentDetailsResponse(
    val paymentId: String,
    val tripId: String,
    val amount: BigDecimal,
    val currency: String,
    val status: String, // "COMPLETED", "PENDING", "FAILED"
    val paymentMethod: String,
    val timestamp: ZonedDateTime
)
