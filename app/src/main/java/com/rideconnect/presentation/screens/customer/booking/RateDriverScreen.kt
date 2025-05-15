package com.rideconnect.presentation.screens.customer.booking

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rideconnect.domain.model.trip.Trip

// Định nghĩa màu sắc chính dựa trên giao diện trong hình ảnh
val PrimaryPurple = Color(0xFF6750A4) // Màu tím chủ đạo từ nút Call
val LightPurple = Color(0xFFF6F2FF) // Màu tím nhạt cho background
val TextGray = Color(0xFF666666) // Màu xám cho text
val StarYellow = Color(0xFFFFB400) // Màu vàng cho sao đánh giá

@Composable
fun RateDriverScreen(
    trip: Trip,
    onRateSubmit: (Int, String) -> Unit,
    onBackClick: () -> Unit
) {
    var rating by remember { mutableStateOf(4) } // Mặc định 4 sao
    var feedback by remember { mutableStateOf("") }
    var selectedTipAmount by remember { mutableStateOf<Double?>(5.0) } // Mặc định 5 VND

    Box(modifier = Modifier.fillMaxSize()) {
        // Header với nút Back
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(56.dp))

            Text(
                text = "Rate Driver",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Driver profile image
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, PrimaryPurple, CircleShape)
            ) {
                // Hiển thị icon mặc định từ Material Icons
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Driver",
                        modifier = Modifier.size(72.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Driver name
            Text(
                text = trip.driverName ?: "Unknown Driver",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Feedback message
            Text(
                text = "Phản hồi của bạn sẽ giúp cải thiện trải nghiệm chuyến đi",
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Star rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                for (i in 1..5) {
                    Icon(
                        imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Star $i",
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { rating = i },
                        tint = if (i <= rating) StarYellow else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Feedback text field
            OutlinedTextField(
                value = feedback,
                onValueChange = { feedback = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = { Text("Nhập phản hồi của bạn tại đây...") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = LightPurple,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = PrimaryPurple
                ),
                trailingIcon = {
                    IconButton(onClick = { /* Send feedback */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = PrimaryPurple
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tip section
            Text(
                text = "Thêm tiền tip cho ${trip.driverName?.split(" ")?.firstOrNull() ?: "Tài xế"}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tip options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TipOption(
                    text = "Không tip",
                    isSelected = selectedTipAmount == 0.0,
                    onClick = { selectedTipAmount = 0.0 }
                )

                TipOption(
                    text = "5.000đ",
                    isSelected = selectedTipAmount == 5.0,
                    onClick = { selectedTipAmount = 5.0 }
                )

                TipOption(
                    text = "10.000đ",
                    isSelected = selectedTipAmount == 10.0,
                    onClick = { selectedTipAmount = 10.0 }
                )

                TipOption(
                    text = "Khác",
                    isSelected = selectedTipAmount != null && selectedTipAmount != 0.0 &&
                            selectedTipAmount != 5.0 && selectedTipAmount != 10.0,
                    onClick = { /* Open custom tip dialog */ }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Submit button
            Button(
                onClick = {
                    // Thêm log khi gửi đánh giá
                    Log.d("RateDriverScreen", "Submitting rating: $rating, feedback: $feedback")
                    Log.d("RateDriverScreen", "Driver: ${trip.driverName}, Trip ID: ${trip.id}")

                    // Gọi callback để gửi đánh giá
                    onRateSubmit(rating, feedback)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple
                )
            ) {
                Text(
                    text = "Hoàn thành",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TipOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) PrimaryPurple else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = if (isSelected) LightPurple else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) PrimaryPurple else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
