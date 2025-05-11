package com.example.testapp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rideconnect.domain.model.driver.Activity
import com.rideconnect.domain.model.driver.ActivityStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun StatusIndicator(status: ActivityStatus) {
    val (backgroundColor, textColor, text) = when (status) {
        ActivityStatus.ACTIVE -> Triple(Green.copy(alpha = 0.2f), Green, "Active")
        ActivityStatus.COMPLETED -> Triple(Color.Blue.copy(alpha = 0.2f), Color.Blue, "Completed")
        ActivityStatus.CANCELLED -> Triple(Color.Red.copy(alpha = 0.2f), Color.Red, "Cancelled")
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ActivityItem(activity: Activity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Tên người dùng
                Text(
                    text = activity.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Thông tin xe
                Text(
                    text = activity.carModel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Thời gian
                Text(
                    text = "Time: ${formatDateTime(activity.time)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }

            // Trạng thái
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        when (activity.status) {
                            ActivityStatus.ACTIVE -> Color(0xFFE8F5E9)
                            ActivityStatus.COMPLETED -> Color(0xFFE3F2FD)
                            ActivityStatus.CANCELLED -> Color(0xFFFFEBEE)
                        }
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = activity.status.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = when (activity.status) {
                        ActivityStatus.ACTIVE -> Color(0xFF2E7D32)
                        ActivityStatus.COMPLETED -> Color(0xFF1565C0)
                        ActivityStatus.CANCELLED -> Color(0xFFC62828)
                    }
                )
            }
        }
    }
}

// Hàm hỗ trợ định dạng thời gian
private fun formatDateTime(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return dateTime.format(formatter)
}
