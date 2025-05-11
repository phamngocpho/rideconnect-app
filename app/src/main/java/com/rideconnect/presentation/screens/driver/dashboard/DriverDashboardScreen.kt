package com.rideconnect.presentation.screens.driver.dashboard

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rideconnect.R

@Composable
fun DriverDashboardScreen(
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status Toggle
            StatusToggle()

            Spacer(modifier = Modifier.height(24.dp))

            // Searching Text
            Text(
                text = "Searching for the client, please wait.",
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Location Indicator
            LocationIndicator()

            Spacer(modifier = Modifier.height(16.dp))

            // Looking for Rider Text
            Text(
                text = "Looking for Rider",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Illustration
            RideIllustration()

            Spacer(modifier = Modifier.weight(1f))
        }

    }
}

// Trong DriverDashboardScreen.kt
@Composable
fun StatusToggle(
    viewModel: DriverDashboardViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    val isOnline by viewModel.isOnline.collectAsState()

    Switch(
        checked = isOnline,
        onCheckedChange = { online ->
            viewModel.toggleOnlineStatus(context, online)
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}


@Preview(showBackground = true)
@Composable
fun StatusTogglePreview() {
    Surface(
        modifier = Modifier.padding(16.dp)
    ) {
        StatusToggle()
    }
}

@Composable
fun LocationIndicator() {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(Color.Transparent)
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .border(
                        width = 2.dp,
                        color = Color(0xFF4CAF50),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationIndicatorPreview() {
    Surface(
        modifier = Modifier.padding(16.dp)
    ) {
        LocationIndicator()
    }
}

@Composable
fun RideIllustration() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        // Tại đây sẽ sử dụng hình ảnh từ resource
        Image(
            painter = painterResource(id = R.drawable.ride_illustration),
            contentDescription = "Ride Illustration",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
        // Lưu ý: Bạn cần thêm hình ảnh vào thư mục res/drawable
    }
}

@Preview(showBackground = true)
@Composable
fun RideIllustrationPreview() {
    // Để preview hoạt động mà không cần hình ảnh thực tế, chúng ta có thể tạo một box màu
    Surface(
        modifier = Modifier.padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Ride Illustration Preview")
        }
    }
}




@Preview(showBackground = true)
@Composable
fun DriverAppPreview() {
    DriverDashboardScreen(
        onLogout = { }
    )
}

// Thêm một preview cho phần "Searching for client" text
@Preview(showBackground = true)
@Composable
fun SearchingTextPreview() {
    Surface(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Searching for the client, please wait.",
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

// Preview cho phần "Looking for Rider" text
@Preview(showBackground = true)
@Composable
fun LookingForRiderTextPreview() {
    Surface(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Looking for Rider",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}
