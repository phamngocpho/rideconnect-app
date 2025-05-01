package com.rideconnect.presentation.components.booking

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rideconnect.domain.model.payment.PaymentMethod
import com.rideconnect.domain.model.payment.PaymentMethodType

@Composable
fun PaymentMethodSelector(
    availablePaymentMethods: List<PaymentMethod>,
    selectedMethod: PaymentMethod,
    onPaymentMethodSelected: (PaymentMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPaymentOptions by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon phương thức thanh toán
        Icon(
            imageVector = selectedMethod.getIcon(),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Tên phương thức thanh toán
        Text(
            text = selectedMethod.getDisplayName(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        // Nút mở dropdown
        IconButton(onClick = { showPaymentOptions = true }) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Chọn phương thức thanh toán"
            )
        }
    }

    // Dialog chọn phương thức thanh toán
    if (showPaymentOptions) {
        AlertDialog(
            onDismissRequest = { showPaymentOptions = false },
            title = { Text("Chọn phương thức thanh toán") },
            text = {
                Column {
                    availablePaymentMethods.forEachIndexed { index, method ->
                        PaymentOption(
                            paymentMethod = method,
                            isSelected = selectedMethod.id == method.id,
                            onClick = {
                                onPaymentMethodSelected(method)
                                showPaymentOptions = false
                            }
                        )

                        if (index < availablePaymentMethods.size - 1) {
                            Divider()
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPaymentOptions = false }) {
                    Text("Đóng")
                }
            }
        )
    }
}

@Composable
private fun PaymentOption(
    paymentMethod: PaymentMethod,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = paymentMethod.getIcon(),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = paymentMethod.getDisplayName(),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
