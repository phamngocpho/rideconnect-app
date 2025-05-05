package com.rideconnect.presentation.screens.driver.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rideconnect.presentation.components.buttons.DriverPrimaryButton

@Composable
fun SuccessScreen(
    onFinishClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Khoảng trống phía trên
        Box(modifier = Modifier.weight(1f))

        // Biểu tượng thành công
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Thành công",
                tint = Color.White,
                modifier = Modifier.size(64.dp)
            )
        }

        // Tiêu đề
        Text(
            text = "Đăng ký thành công!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
        )

        // Mô tả
        Text(
            text = "Chúc mừng! Bạn đã hoàn thành quá trình đăng ký. Tài khoản của bạn đang được xem xét và sẽ được kích hoạt trong vòng 24 giờ.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        // Khoảng trống giữa
        Box(modifier = Modifier.weight(1f))

        // Nút hoàn thành
        DriverPrimaryButton(
            text = "Bắt đầu",
            onClick = onFinishClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
    }
}
