package com.rideconnect.data.remote.dto.request.payment

data class CreatePaymentRequest(
    val tripId: String,
    val amount: Double,
    val paymentMethodId: String,
    val currency: String = "VND"
)
