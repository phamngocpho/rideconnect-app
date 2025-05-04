package com.rideconnect.domain.model.payment

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.ZonedDateTime
import java.util.Locale

// Enum cơ bản cho các loại phương thức thanh toán
enum class PaymentMethodType {
    CREDIT_CARD,
    DEBIT_CARD,
    PAYPAL,
    CASH,
    PROMOTION,
    WALLET,
    OTHER
}

// Class chính cho phương thức thanh toán
data class PaymentMethod(
    val id: String,
    val userId: String? = null,
    val type: PaymentMethodType,
    val cardNumber: String? = null, // Last 4 digits only
    val cardBrand: String? = null,
    val expiryMonth: Int? = null,
    val expiryYear: Int? = null,
    val isDefault: Boolean = false,
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val promotionInfo: PromotionInfo? = null
) {
    fun getDisplayName(): String {
        return when (type) {
            PaymentMethodType.CREDIT_CARD -> "$cardBrand ****$cardNumber"
            PaymentMethodType.DEBIT_CARD -> "$cardBrand ****$cardNumber"
            PaymentMethodType.PAYPAL -> "PayPal"
            PaymentMethodType.CASH -> "Tiền mặt"
            PaymentMethodType.PROMOTION -> promotionInfo?.name ?: "Ưu đãi"
            PaymentMethodType.WALLET -> "Ví điện tử"
            PaymentMethodType.OTHER -> "Phương thức khác"
        }
    }

    fun getIcon(): ImageVector {
        return when (type) {
            PaymentMethodType.CASH -> Icons.Default.Money
            PaymentMethodType.PAYPAL -> Icons.Default.AccountBalance
            PaymentMethodType.CREDIT_CARD, PaymentMethodType.DEBIT_CARD -> Icons.Default.CreditCard
            PaymentMethodType.PROMOTION -> Icons.Default.Percent
            PaymentMethodType.WALLET -> Icons.Default.AccountBalanceWallet
            PaymentMethodType.OTHER -> Icons.Default.Payment
        }
    }

    fun isExpired(): Boolean {
        if (type != PaymentMethodType.CREDIT_CARD && type != PaymentMethodType.DEBIT_CARD) {
            return false
        }

        val now = ZonedDateTime.now()
        val currentYear = now.year
        val currentMonth = now.monthValue

        return (expiryYear ?: 0) < currentYear ||
                ((expiryYear ?: 0) == currentYear && (expiryMonth ?: 0) < currentMonth)
    }

    fun getExpiryText(): String {
        return if (expiryMonth != null && expiryYear != null) {
            String.format(Locale.US, "%02d/%d", expiryMonth, expiryYear % 100)
        } else {
            ""
        }
    }

    companion object {
        // Factory methods cho các phương thức thanh toán phổ biến
        fun cash() = PaymentMethod(
            id = "cash",
            type = PaymentMethodType.CASH,
            isDefault = true
        )

        fun promotion(info: PromotionInfo) = PaymentMethod(
            id = "promotion_${info.id}",
            type = PaymentMethodType.PROMOTION,
            promotionInfo = info
        )

        fun wallet() = PaymentMethod(
            id = "wallet",
            type = PaymentMethodType.WALLET
        )
    }
}

// Class mới để lưu thông tin về khuyến mãi
data class PromotionInfo(
    val id: String,
    val name: String,
    val description: String,
    val discountAmount: Double = 0.0,
    val discountPercent: Double = 0.0,
    val maxDiscount: Double? = null,
    val expiryDate: ZonedDateTime? = null,
    val code: String? = null
) {
    fun isValid(): Boolean {
        return expiryDate?.isAfter(ZonedDateTime.now()) ?: true
    }

    fun getDiscountText(): String {
        return when {
            discountPercent > 0 -> "Giảm ${discountPercent.toInt()}%"
            discountAmount > 0 -> "Giảm ${discountAmount.toLong()}đ"
            else -> "Ưu đãi đặc biệt"
        }
    }
}

// UI State cho Payment Method List Screen
data class PaymentMethodListUiState(
    val paymentMethods: List<PaymentMethodUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showAddPaymentMethodDialog: Boolean = false
)

data class PaymentMethodUiModel(
    val paymentMethod: PaymentMethod,
    val isSelected: Boolean = false
)

// UI State cho Add Payment Method Screen
data class AddPaymentMethodUiState(
    val type: PaymentMethodType = PaymentMethodType.CREDIT_CARD,
    val cardNumber: String = "",
    val cardHolderName: String = "",
    val expiryMonth: String = "",
    val expiryYear: String = "",
    val cvv: String = "",
    val isDefault: Boolean = false,
    val isProcessing: Boolean = false,
    val error: String? = null,
    val isValid: Boolean = false
)

// UI State cho Payment Selection trong Vehicle Selection Screen
data class PaymentSelectionUiState(
    val availablePaymentMethods: List<PaymentMethod> = listOf(PaymentMethod.cash()),
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.cash(),
    val availablePromotions: List<PromotionInfo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
