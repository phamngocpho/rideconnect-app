package com.rideconnect.presentation.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Trạng thái của mã khuyến mãi
 */
enum class PromoStatus {
    NONE,
    VALID,
    INVALID
}

/**
 * Component nhập mã khuyến mãi
 * @param promoCode Mã khuyến mãi hiện tại
 * @param onPromoCodeChanged Callback khi thay đổi mã khuyến mãi
 * @param onApplyPromo Callback khi áp dụng mã khuyến mãi
 * @param modifier Modifier để tùy chỉnh layout
 * @param promoStatus Trạng thái của mã khuyến mãi
 * @param promoMessage Thông báo liên quan đến mã khuyến mãi (thành công/thất bại)
 */
@Composable
fun PromoCodeInput(
    promoCode: String,
    onPromoCodeChanged: (String) -> Unit,
    onApplyPromo: () -> Unit,
    modifier: Modifier = Modifier,
    promoStatus: PromoStatus = PromoStatus.NONE,
    promoMessage: String = ""
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Sử dụng Row với weight để đảm bảo tỷ lệ phù hợp
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // TextField chiếm phần lớn không gian
            OutlinedTextField(
                value = promoCode,
                onValueChange = onPromoCodeChanged,
                label = { Text("Mã khuyến mãi") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                isError = promoStatus == PromoStatus.INVALID,
                textStyle = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Button với kích thước cố định
            Button(
                onClick = onApplyPromo,
                enabled = promoCode.isNotBlank(),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Áp dụng",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Thêm khoảng cách trước thông báo
        Spacer(modifier = Modifier.height(8.dp))

        // Hiển thị thông báo nếu có
        if (promoStatus != PromoStatus.NONE && promoMessage.isNotBlank()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            ) {
                when (promoStatus) {
                    PromoStatus.VALID -> {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Valid Promo",
                            tint = Color.Green,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(
                            text = promoMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Green
                        )
                    }
                    PromoStatus.INVALID -> {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Invalid Promo",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(
                            text = promoMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    else -> { /* Không hiển thị gì nếu status là NONE */ }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PromoCodeInputPreview() {
    var promoCode by remember { mutableStateOf("") }
    var promoStatus by remember { mutableStateOf(PromoStatus.NONE) }
    var promoMessage by remember { mutableStateOf("") }

    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            // Default state
            PromoCodeInput(
                promoCode = promoCode,
                onPromoCodeChanged = { promoCode = it },
                onApplyPromo = {
                    if (promoCode == "SAVE20") {
                        promoStatus = PromoStatus.VALID
                        promoMessage = "Giảm giá 20.000đ đã được áp dụng!"
                    } else {
                        promoStatus = PromoStatus.INVALID
                        promoMessage = "Mã khuyến mãi không hợp lệ hoặc đã hết hạn"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                promoStatus = promoStatus,
                promoMessage = promoMessage
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Valid state preview
            PromoCodeInput(
                promoCode = "SAVE20",
                onPromoCodeChanged = { },
                onApplyPromo = { },
                modifier = Modifier.fillMaxWidth(),
                promoStatus = PromoStatus.VALID,
                promoMessage = "Giảm giá 20.000đ đã được áp dụng!"
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Invalid state preview
            PromoCodeInput(
                promoCode = "INVALID",
                onPromoCodeChanged = { },
                onApplyPromo = { },
                modifier = Modifier.fillMaxWidth(),
                promoStatus = PromoStatus.INVALID,
                promoMessage = "Mã khuyến mãi không hợp lệ hoặc đã hết hạn"
            )
        }
    }
}
