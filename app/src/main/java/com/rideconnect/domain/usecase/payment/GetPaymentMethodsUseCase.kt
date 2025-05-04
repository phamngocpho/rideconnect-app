package com.rideconnect.domain.usecase.payment

import com.rideconnect.domain.model.payment.PaymentMethod
import com.rideconnect.domain.model.payment.PaymentMethodType
import com.rideconnect.domain.model.payment.PromotionInfo
import java.time.ZonedDateTime
import javax.inject.Inject

class GetPaymentMethodsUseCase @Inject constructor() {

    suspend operator fun invoke(): List<PaymentMethod> {
        // Trả về danh sách các phương thức thanh toán mẫu
        return listOf(
            // Tiền mặt (mặc định)
            PaymentMethod.cash(),

            // Thẻ tín dụng
            PaymentMethod(
                id = "card1",
                type = PaymentMethodType.CREDIT_CARD,
                cardNumber = "1234",
                cardBrand = "Visa",
                expiryMonth = 12,
                expiryYear = 2025,
                isDefault = false
            ),

            // Thẻ ghi nợ
            PaymentMethod(
                id = "card2",
                type = PaymentMethodType.DEBIT_CARD,
                cardNumber = "5678",
                cardBrand = "Mastercard",
                expiryMonth = 10,
                expiryYear = 2026,
                isDefault = false
            ),

            // Ví điện tử
            PaymentMethod.wallet(),

            // Khuyến mãi
            PaymentMethod.promotion(
                PromotionInfo(
                    id = "promo1",
                    name = "Giảm 20% Chuyến Đầu",
                    description = "Giảm 20% cho chuyến đi đầu tiên, tối đa 50.000đ",
                    discountPercent = 20.0,
                    maxDiscount = 50000.0,
                    expiryDate = ZonedDateTime.now().plusDays(30),
                    code = "WELCOME20"
                )
            )
        )
    }
}
