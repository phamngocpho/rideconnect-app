package com.rideconnect.domain.model.payment

import com.rideconnect.domain.model.trip.Trip
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class Payment(
    val id: String,
    val tripId: String,
    val customerId: String,
    val paymentMethodId: String,
    val amount: BigDecimal,
    val status: PaymentStatus,
    val transactionId: String?,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime?,
    val paymentMethod: PaymentMethod? = null
) {
    fun getFormattedAmount(): String {
        return NumberFormat.getCurrencyInstance().format(amount)
    }

    fun getStatusText(): String {
        return when (status) {
            PaymentStatus.PENDING -> "Pending"
            PaymentStatus.COMPLETED -> "Completed"
            PaymentStatus.FAILED -> "Failed"
            PaymentStatus.REFUNDED -> "Refunded"
        }
    }

    fun getFormattedDate(): String {
        return createdAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"))
    }

    fun isSuccessful(): Boolean {
        return status == PaymentStatus.COMPLETED
    }
}

enum class PaymentStatus {
    PENDING, COMPLETED, FAILED, REFUNDED
}

// UI State cho Payment List Screen
data class PaymentListUiState(
    val payments: List<Payment> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalAmount: BigDecimal = BigDecimal.ZERO
)

// UI State cho Payment Detail Screen
data class PaymentDetailUiState(
    val payment: Payment? = null,
    val trip: Trip? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
