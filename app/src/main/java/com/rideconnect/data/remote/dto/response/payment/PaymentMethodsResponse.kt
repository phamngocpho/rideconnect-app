package com.rideconnect.data.remote.dto.response.payment

data class PaymentMethodsResponse(
    val paymentMethods: List<PaymentMethod>
) {
    data class PaymentMethod(
        val id: String,
        val type: String, // "CREDIT_CARD", "MOMO", "ZALOPAY", etc.
        val lastFourDigits: String?,
        val cardType: String?,
        val expiryDate: String?,
        val isDefault: Boolean,
        val walletName: String?,
        val walletPhoneNumber: String?
    )
}
