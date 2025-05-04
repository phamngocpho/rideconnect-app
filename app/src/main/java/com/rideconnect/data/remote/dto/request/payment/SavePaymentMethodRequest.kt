package com.rideconnect.data.remote.dto.request.payment

data class SavePaymentMethodRequest(
    val type: String, // "CREDIT_CARD", "MOMO", "ZALOPAY", etc.
    val token: String,
    val isDefault: Boolean = false
)
