package com.rideconnect.presentation.components.location

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rideconnect.domain.model.location.Location

@Composable
fun LocationInfoCard(
    location: Location, // Đã thay đổi từ sourceLocation thành location
    modifier: Modifier = Modifier,
    onConfirmClick: (() -> Unit)? = null // Thêm tham số tùy chọn
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Địa điểm",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = location.address ?: "Không có địa chỉ",
                style = MaterialTheme.typography.bodyMedium
            )

            // Hiển thị tọa độ nếu có
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tọa độ: ${location.latitude}, ${location.longitude}",
                style = MaterialTheme.typography.bodySmall
            )

            // Chỉ hiển thị nút xác nhận nếu onConfirmClick không null
            onConfirmClick?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = it,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Xác nhận")
                }
            }
        }
    }
}

// Thêm overload function để tương thích với code cũ
@Composable
fun LocationInfoCard(
    sourceLocation: Location?,
    destinationLocation: Location?,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Điểm đón",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = sourceLocation?.address ?: "Không có địa chỉ",
                style = MaterialTheme.typography.bodyMedium
            )

            if (destinationLocation != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Điểm đến",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = destinationLocation.address ?: "Không có địa chỉ",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onConfirmClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Xác nhận")
            }
        }
    }
}
