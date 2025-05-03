package com.rideconnect.presentation.components.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rideconnect.domain.model.vehicle.Vehicle
import com.rideconnect.presentation.components.common.VehicleIcon

@Composable
fun VehicleItem(
    vehicle: Vehicle,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            // Tăng alpha lên 0.15f để đậm hơn (từ 0.1f)
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon xe
            VehicleIcon(
                vehicleType = vehicle.type,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Thông tin chính
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = vehicle.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Đón trong ${vehicle.estimatedPickupTime} phút",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Giá
            Text(
                text = "${vehicle.price}₫",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

